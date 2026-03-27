<template>
  <div class="admin-panel-view">
    <!-- Admin Stats Row -->
    <div class="admin-stats-row">
      <div class="admin-stat-card">
        <div class="admin-stat-label">Total Demandes</div>
        <div class="admin-stat-value">{{ allRequests.length }}</div>
      </div>
      <div class="admin-stat-card">
        <div class="admin-stat-label">À Traiter</div>
        <div class="admin-stat-value" style="color:var(--warning)">{{ pendingRequests.length }}</div>
      </div>
      <div class="admin-stat-card">
        <div class="admin-stat-label">Approuvées</div>
        <div class="admin-stat-value" style="color:var(--success)">{{ approvedCount }}</div>
      </div>
      <div class="admin-stat-card">
        <div class="admin-stat-label">Taux d'absence</div>
        <div class="admin-stat-value">{{ absenceRate }}%</div>
      </div>
    </div>

    <!-- Pending Requests Table -->
    <div class="card table-container">
      <div class="admin-header">
        <h3 class="section-title" style="margin:0">
          <i class="fas fa-user-shield"></i> Demandes à Traiter
        </h3>
        <span class="badge badge-warning">{{ pendingRequests.length }} en attente</span>
      </div>

      <!-- Toolbar -->
      <div class="table-toolbar">
        <div class="search-wrapper">
          <i class="fas fa-search"></i>
          <input type="text" v-model="search" placeholder="Rechercher un employé..." class="input">
        </div>
        <div class="filter-wrapper">
          <select v-model="filterType" class="select select-sm">
            <option value="ALL">Tous les types</option>
            <option v-for="t in uniqueTypes" :key="t" :value="t">{{ t }}</option>
          </select>
        </div>
      </div>

      <!-- Table -->
      <div class="table-responsive">
        <table class="data-table">
          <thead>
            <tr>
              <th>Employé</th>
              <th>Type &amp; Motif</th>
              <th>Période</th>
              <th>Solde Reste</th>
              <th class="text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="5" class="text-center py-12 text-gray-400">
                <i class="fas fa-spinner fa-spin mr-2"></i> Chargement...
              </td>
            </tr>
            <tr v-else-if="filteredRequests.length === 0">
              <td colspan="5">
                <div class="empty-state">
                  <svg width="100" height="100" viewBox="0 0 200 200" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <circle cx="100" cy="100" r="70" fill="#F8FAFC" stroke="#CBD5E1" stroke-width="3"></circle>
                    <path d="M80 110L95 125L125 85" stroke="#10B981" stroke-width="6" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                  <p>Toutes les demandes ont été traitées !</p>
                </div>
              </td>
            </tr>
            <tr v-for="req in filteredRequests" :key="req.id">
              <td data-label="Employé">
                <div class="user-info-mini">
                  <div class="user-avatar-mini">{{ getInitials(req.employeId) }}</div>
                  <div class="user-details-mini">
                    <div class="user-name-mini">{{ req.employeId }}</div>
                    <div class="user-dept-mini">Département RH</div>
                  </div>
                </div>
              </td>
              <td data-label="Type">
                <div>
                  <strong class="type-cell">{{ req.typeConge.libelle }}</strong>
                  <div v-if="req.motif" style="font-size:0.75rem;color:var(--text-gray);margin-top:2px">
                    {{ truncate(req.motif, 40) }}
                  </div>
                </div>
              </td>
              <td class="period-cell" data-label="Période">
                {{ formatDate(req.dateDebut) }} <i class="fas fa-arrow-right"></i> {{ formatDate(req.dateFin) }}
                <div style="font-size:0.6875rem;color:var(--text-gray)">{{ req.dureeJoursOuvres }} jour{{ req.dureeJoursOuvres > 1 ? 's' : '' }}</div>
              </td>
              <td data-label="Solde">
                <strong style="color:var(--primary-blue)">—</strong>
              </td>
              <td class="text-right actions-cell" data-label="Actions">
                <button class="btn btn-icon btn-approve" title="Valider" @click="processRequest(req, 'valider')">
                  <i class="fas fa-check"></i>
                </button>
                <button class="btn btn-icon btn-reject" title="Refuser" @click="processRequest(req, 'refuser')">
                  <i class="fas fa-times"></i>
                </button>
                <button class="btn btn-icon btn-view" title="Détails" @click="viewDetail(req)">
                  <i class="fas fa-eye"></i>
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
    allRequests: [],
    pendingRequests: [],
    loading: true,
    search: '',
    filterType: 'ALL'
  }),
  computed: {
    approvedCount() {
      return this.allRequests.filter(r => r.statut === 'VALIDEE').length;
    },
    absenceRate() {
      if (this.allRequests.length === 0) return '0.0';
      return ((this.approvedCount / Math.max(this.allRequests.length, 1)) * 100).toFixed(1);
    },
    uniqueTypes() {
      const types = this.pendingRequests.map(r => r.typeConge?.libelle).filter(Boolean);
      return [...new Set(types)];
    },
    filteredRequests() {
      return this.pendingRequests.filter(r => {
        const q = this.search.toLowerCase();
        const matchSearch = !q || r.employeId.toLowerCase().includes(q);
        const matchType = this.filterType === 'ALL' || r.typeConge?.libelle === this.filterType;
        return matchSearch && matchType;
      });
    }
  },
  created() {
    this.fetchRequests();
  },
  methods: {
    async fetchRequests() {
      this.loading = true;
      try {
        const [pendingResp, allResp] = await Promise.allSettled([
          apiService.getDemandesATraiter(),
          apiService.getToutesLesDemandes()
        ]);
        if (pendingResp.status === 'fulfilled') {
          this.pendingRequests = pendingResp.value.data || [];
        }
        if (allResp.status === 'fulfilled') {
          this.allRequests = allResp.value.data || [];
        } else {
          this.allRequests = this.pendingRequests;
        }
      } catch (e) {
        this.$emit('show-notification', "Erreur lors du chargement des demandes", "error");
      } finally {
        this.loading = false;
      }
    },
    async processRequest(req, action) {
      const label = action === 'valider' ? 'validation' : 'refus';
      const comment = prompt(`Commentaire pour cette ${label} (optionnel) :`);
      if (comment === null) return;

      try {
        if (action === 'valider') {
          await apiService.validerDemande(req.id, comment);
          this.$emit('show-notification', "Demande validée avec succès !");
        } else {
          await apiService.refuserDemande(req.id, comment);
          this.$emit('show-notification', "Demande refusée.", "warning");
        }
        this.fetchRequests();
      } catch (e) {
        this.$emit('show-notification', "Erreur lors du traitement.", "error");
      }
    },
    viewDetail(req) {
      alert(`Détails de la demande #${req.id}\n\nEmployé: ${req.employeId}\nType: ${req.typeConge.libelle}\nDu ${req.dateDebut} au ${req.dateFin}\nDurée: ${req.dureeJoursOuvres} jours\nMotif: ${req.motif || 'Non spécifié'}`);
    },
    getInitials(id) {
      if (!id) return '?';
      return id.substring(0, 2).toUpperCase();
    },
    truncate(str, max) {
      if (!str) return '';
      return str.length > max ? str.substring(0, max) + '...' : str;
    },
    formatDate(dateStr) {
      if (!dateStr) return 'N/A';
      return new Date(dateStr).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short' });
    }
  }
};
</script>
