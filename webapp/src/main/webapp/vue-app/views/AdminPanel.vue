<template>
  <div class="admin-panel-view">
    <div class="card">
       <div class="admin-header">
          <h3 class="section-title">
            <i class="fas fa-user-shield"></i>
            Demandes à Traiter
          </h3>
          <span class="badge badge-info">{{ requests.length }} EN ATTENTE</span>
       </div>

       <div class="table-responsive">
          <table class="data-table">
            <thead>
              <tr>
                <th>Collaborateur</th>
                <th>Période</th>
                <th>Type</th>
                <th>Durée</th>
                <th>Statut</th>
                <th class="text-right">Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="loading">
                 <td colspan="6" class="text-center py-12 text-gray-400">
                    <i class="fas fa-spinner fa-spin mr-2"></i>Chargement...
                 </td>
              </tr>
              <tr v-else-if="requests.length === 0">
                 <td colspan="6" class="text-center py-12 text-gray-500">Aucune demande en attente de validation.</td>
              </tr>
              <tr v-for="req in requests" :key="req.id">
                <td class="user-cell" data-label="Collaborateur">
                   <div class="user-info-mini">
                      <div class="user-avatar-mini">
                         {{ req.employeId.substring(0, 2).toUpperCase() }}
                      </div>
                      <span class="user-name-mini">{{ req.employeId }}</span>
                   </div>
                </td>
                <td class="period-cell" data-label="Période">{{ formatDate(req.dateDebut) }} <i class="fas fa-arrow-right"></i> {{ formatDate(req.dateFin) }}</td>
                <td class="type-cell" data-label="Type">{{ req.typeConge.libelle }}</td>
                <td class="duration-cell" data-label="Durée"><strong>{{ req.dureeJoursOuvres }}j</strong></td>
                <td data-label="Statut"><span class="status-badge status-pending">EN ATTENTE</span></td>
                <td class="text-right actions-cell" data-label="Actions">
                   <button class="btn btn-secondary" @click="processRequest(req, 'valider')">Valider</button>
                   <button class="btn btn-outline btn-danger" @click="processRequest(req, 'refuser')">Refuser</button>
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
    requests: [],
    loading: true
  }),
  created() {
    this.fetchRequests();
  },
  methods: {
    async fetchRequests() {
      this.loading = true;
      try {
        const resp = await apiService.getDemandesATraiter();
        this.requests = resp.data;
      } catch (e) {
        this.$root.$children[0]?.showNotification("Erreur lors du chargement des demandes", "error");
      } finally {
        this.loading = false;
      }
    },
    async processRequest(req, action) {
       const comment = prompt(`Commentaire pour cette ${action === 'valider' ? 'validation' : 'refus'} (optionnel) :`);
       if (comment === null) return;

       try {
         if (action === 'valider') {
           await apiService.validerDemande(req.id, comment);
           this.$root.$children[0]?.showNotification("Demande validée avec succès.");
         } else {
           await apiService.refuserDemande(req.id, comment);
           this.$root.$children[0]?.showNotification("Demande refusée.", "warning");
         }
         this.fetchRequests();
       } catch (e) {
         this.$root.$children[0]?.showNotification("Erreur lors du traitement.", "error");
       }
    },
    formatDate(dateStr) {
       if (!dateStr) return 'N/A';
       return new Date(dateStr).toLocaleDateString();
    }
  }
};
</script>
