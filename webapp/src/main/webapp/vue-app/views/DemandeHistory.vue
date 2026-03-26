<template>
  <div class="demande-history-view">
    <div class="card table-container">
      <!-- Search / Filters -->
      <div class="table-toolbar">
         <div class="search-wrapper">
            <i class="fas fa-search"></i>
            <input type="text" v-model="search" placeholder="Rechercher par motif ou date..." class="input">
         </div>
         <div class="filter-wrapper">
            <select v-model="filterStatus" class="select select-sm">
               <option value="ALL">Tous les statuts</option>
               <option value="VALIDEE">Validées</option>
               <option value="EN_ATTENTE">En attente</option>
               <option value="REFUSEE">Refusées</option>
               <option value="ANNULEE">Annulées</option>
            </select>
         </div>
      </div>

      <!-- Data Table -->
      <div class="table-responsive">
        <table class="data-table">
          <thead>
            <tr>
              <th>Période</th>
              <th>Type</th>
              <th>Durée</th>
              <th>Statut</th>
              <th>Date Soumission</th>
              <th class="text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
               <td colspan="6" class="text-center py-12 text-gray-400">
                  <i class="fas fa-spinner fa-spin mr-2"></i> Chargement de votre historique...
               </td>
            </tr>
            <tr v-else-if="filteredDemandes.length === 0">
               <td colspan="6" class="text-center py-12 text-gray-500 italic">
                  Aucune demande trouvée.
               </td>
            </tr>
            <tr v-for="demande in filteredDemandes" :key="demande.id">
              <td class="period-cell" data-label="Période">
                {{ formatDate(demande.dateDebut) }} <i class="fas fa-arrow-right"></i> {{ formatDate(demande.dateFin) }}
              </td>
              <td class="type-cell" data-label="Type">
                {{ demande.typeConge.libelle }}
              </td>
              <td class="duration-cell" data-label="Durée">
                <strong>{{ demande.dureeJoursOuvres }}j</strong>
              </td>
              <td data-label="Statut">
                <span class="status-badge" :class="getBadgeClass(demande.statut)">
                   {{ demande.statut }}
                </span>
              </td>
              <td class="date-cell" data-label="Soumise le">
                {{ formatDate(demande.dateSoumission) }}
              </td>
              <td class="text-right actions-cell" data-label="Actions">
                <button v-if="demande.statut === 'EN_ATTENTE'" class="btn btn-icon" title="Annuler" @click="cancelDemande(demande.id)">
                   <i class="fas fa-times"></i>
                </button>
             </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';

export default {
  data: () => ({
    demandes: [],
    search: '',
    filterStatus: 'ALL',
    loading: true
  }),
  computed: {
    filteredDemandes() {
      return this.demandes.filter(d => {
        const motifMatch = d.motif && d.motif.toLowerCase().includes(this.search.toLowerCase());
        const dateMatch = d.dateDebut.includes(this.search);
        const matchesSearch = motifMatch || dateMatch;
        const matchesStatus = this.filterStatus === 'ALL' || d.statut === this.filterStatus;
        return matchesSearch && matchesStatus;
      });
    }
  },
  created() {
    this.fetchDemandes();
  },
  methods: {
    async fetchDemandes() {
      this.loading = true;
      try {
        const resp = await apiService.getMesDemandes();
        this.demandes = resp.data;
      } catch (e) {
        this.$root.$children[0]?.showNotification("Erreur lors du chargement de l'historique", "error");
      } finally {
        this.loading = false;
      }
    },
    async cancelDemande(id) {
       if (confirm("Voulez-vous vraiment annuler cette demande ?")) {
          try {
            await apiService.annulerDemande(id);
            this.$root.$children[0]?.showNotification("Demande annulée.");
            this.fetchDemandes();
          } catch (e) {
            this.$root.$children[0]?.showNotification("Erreur lors de l'annulation.", "error");
          }
       }
    },
    formatDate(dateStr) {
      if (!dateStr) return 'N/A';
      return new Date(dateStr).toLocaleDateString();
    },
    getBadgeClass(statut) {
      return {
        'status-valid': statut === 'VALIDEE',
        'status-pending': statut === 'EN_ATTENTE',
        'status-error': statut === 'REFUSEE' || statut === 'ANNULEE'
      };
    }
  }
};
</script>
