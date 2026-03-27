<template>
  <div class="demande-history-view">
    <div class="card table-container">
      <!-- Toolbar -->
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

      <!-- Table -->
      <div class="table-responsive">
        <table class="data-table">
          <thead>
            <tr>
              <th>N° Demande</th>
              <th>Type</th>
              <th>Période</th>
              <th>Durée</th>
              <th>Statut</th>
              <th class="text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="6" class="text-center py-12 text-gray-400">
                <i class="fas fa-spinner fa-spin mr-2"></i> Chargement...
              </td>
            </tr>
            <tr v-else-if="paginatedDemandes.length === 0">
              <td colspan="6">
                <div class="empty-state">
                  <svg width="120" height="120" viewBox="0 0 200 200" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <rect x="40" y="40" width="120" height="120" rx="12" fill="#F8FAFC" stroke="#CBD5E1" stroke-width="3"></rect>
                    <line x1="65" y1="75" x2="135" y2="75" stroke="#CBD5E1" stroke-width="4" stroke-linecap="round"></line>
                    <line x1="65" y1="100" x2="135" y2="100" stroke="#CBD5E1" stroke-width="4" stroke-linecap="round"></line>
                    <line x1="65" y1="125" x2="105" y2="125" stroke="#CBD5E1" stroke-width="4" stroke-linecap="round"></line>
                  </svg>
                  <p>Votre historique de demandes est vide.</p>
                  <button class="btn btn-primary btn-sm" @click="$emit('change-view', 'DemandeForm')">
                    <i class="fas fa-plus"></i> Faire ma première demande
                  </button>
                </div>
              </td>
            </tr>
            <tr v-for="(demande, index) in paginatedDemandes" :key="demande.id">
              <td data-label="N° Demande">
                <strong style="color:var(--primary-blue)">#REQ-{{ currentYear }}-{{ String(startIndex + index + 1).padStart(3, '0') }}</strong>
              </td>
              <td class="type-cell" data-label="Type">{{ getTypeName(demande) }}</td>
              <td class="period-cell" data-label="Période">
                {{ formatDate(demande.dateDebut) }} <i class="fas fa-arrow-right"></i> {{ formatDate(demande.dateFin) }}
              </td>
              <td class="duration-cell" data-label="Durée">
                <strong>{{ demande.dureeJoursOuvres }} jour{{ demande.dureeJoursOuvres > 1 ? 's' : '' }}</strong>
              </td>
              <td data-label="Statut">
                <span class="status-badge" :class="getBadgeClass(demande.statut)">{{ formatStatus(demande.statut) }}</span>
              </td>
              <td class="text-right actions-cell" data-label="Actions">
                <button v-if="demande.statut === 'EN_ATTENTE'" class="btn btn-icon btn-reject" title="Annuler" @click="cancelDemande(demande.id)">
                  <i class="fas fa-times"></i>
                </button>
                <span v-else style="font-size:0.75rem;color:var(--text-muted)">—</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Pagination -->
      <div v-if="filteredDemandes.length > 0" class="table-footer">
        <span>Affichage {{ startIndex + 1 }} à {{ endIndex }} sur {{ filteredDemandes.length }} entrées</span>
        <div class="pagination">
          <button :disabled="currentPage === 1" @click="currentPage--"><i class="fas fa-chevron-left"></i></button>
          <button
            v-for="page in totalPages"
            :key="page"
            :class="{ active: currentPage === page }"
            @click="currentPage = page"
          >{{ page }}</button>
          <button :disabled="currentPage === totalPages" @click="currentPage++"><i class="fas fa-chevron-right"></i></button>
        </div>
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
    loading: true,
    currentPage: 1,
    perPage: 8,
    currentYear: new Date().getFullYear()
  }),
  computed: {
    filteredDemandes() {
      return this.demandes.filter(d => {
        const q = this.search.toLowerCase();
        const motifMatch = d.motif && d.motif.toLowerCase().includes(q);
        const dateMatch = d.dateDebut && d.dateDebut.includes(this.search);
        const typeMatch = this.getTypeName(d).toLowerCase().includes(q);
        const matchesSearch = !q || motifMatch || dateMatch || typeMatch;
        const matchesStatus = this.filterStatus === 'ALL' || d.statut === this.filterStatus;
        return matchesSearch && matchesStatus;
      });
    },
    totalPages() {
      return Math.max(1, Math.ceil(this.filteredDemandes.length / this.perPage));
    },
    startIndex() {
      return (this.currentPage - 1) * this.perPage;
    },
    endIndex() {
      return Math.min(this.startIndex + this.perPage, this.filteredDemandes.length);
    },
    paginatedDemandes() {
      return this.filteredDemandes.slice(this.startIndex, this.endIndex);
    }
  },
  watch: {
    search() { this.currentPage = 1; },
    filterStatus() { this.currentPage = 1; }
  },
  created() {
    this.fetchDemandes();
  },
  methods: {
    async fetchDemandes() {
      this.loading = true;
      try {
        const resp = await apiService.getMesDemandes();
        this.demandes = resp.data || [];
      } catch (e) {
        this.$emit('show-notification', "Erreur lors du chargement de l'historique", "error");
      } finally {
        this.loading = false;
      }
    },
    async cancelDemande(id) {
      /* Use custom modal from parent */
      const parent = this.$root.$children[0];
      if (parent && parent.showConfirm) {
        const confirmed = await parent.showConfirm({
          title: 'Annuler la demande ?',
          message: 'Cette action est irréversible. La demande sera définitivement annulée.',
          icon: 'fas fa-exclamation-triangle',
          type: 'danger',
          confirmText: 'Oui, annuler',
          btnClass: 'btn-danger'
        });
        if (!confirmed) return;
      }

      try {
        await apiService.annulerDemande(id);
        this.$emit('show-notification', "Demande annulée avec succès.");
        /* Refresh the list to update the status */
        await this.fetchDemandes();
      } catch (e) {
        this.$emit('show-notification', "Erreur lors de l'annulation.", "error");
      }
    },
    getTypeName(demande) {
      if (demande.typeConge && demande.typeConge.libelle) {
        return demande.typeConge.libelle;
      }
      if (demande.typeConge && demande.typeConge.id) {
        return 'Type #' + demande.typeConge.id;
      }
      return 'Congé';
    },
    formatDate(dateStr) {
      if (!dateStr) return 'N/A';
      return new Date(dateStr).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
    },
    formatStatus(statut) {
      const labels = { 'EN_ATTENTE': 'En attente', 'VALIDEE': 'Validée', 'REFUSEE': 'Refusée', 'ANNULEE': 'Annulée' };
      return labels[statut] || statut;
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
