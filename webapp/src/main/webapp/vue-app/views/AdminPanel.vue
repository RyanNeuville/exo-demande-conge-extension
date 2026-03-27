<template>
  <div class="admin-panel-view">
    <!-- Admin Hero Section (Stats + Mini Graph) -->
    <div class="admin-hero">
      <div class="admin-stats-row">
        <div class="admin-stat-card border-blue">
          <div class="stat-icon-mini blue-glow"><i class="fas fa-database"></i></div>
          <div class="stat-info">
            <div class="admin-stat-label">Total Demandes</div>
            <div class="admin-stat-value">{{ allRequests.length }}</div>
          </div>
        </div>
        <div class="admin-stat-card border-orange">
          <div class="stat-icon-mini orange-glow"><i class="fas fa-clock"></i></div>
          <div class="stat-info">
            <div class="admin-stat-label">À Traiter</div>
            <div class="admin-stat-value">{{ pendingRequests.length }}</div>
          </div>
        </div>
        <div class="admin-stat-card border-green">
          <div class="stat-icon-mini green-glow"><i class="fas fa-check-double"></i></div>
          <div class="stat-info">
            <div class="admin-stat-label">Approuvées</div>
            <div class="admin-stat-value">{{ approvedCount }}</div>
          </div>
        </div>
        <div class="admin-stat-card border-red">
          <div class="stat-icon-mini red-glow"><i class="fas fa-chart-pie"></i></div>
          <div class="stat-info">
            <div class="admin-stat-label">Taux d'absence</div>
            <div class="admin-stat-value">{{ absenceRate }}%</div>
          </div>
        </div>
      </div>

      <!-- Donut Chart Card -->
      <div class="card chart-card">
        <h4 class="chart-title">Répartition des Demandes</h4>
        <div class="chart-container">
          <svg width="120" height="120" viewBox="0 0 40 40" class="donut">
            <circle class="donut-hole" cx="20" cy="20" r="15.915" fill="transparent"></circle>
            <circle class="donut-ring" cx="20" cy="20" r="15.915" fill="transparent" stroke="#F1F5F9" stroke-width="4"></circle>
            
            <!-- Approved Segment (Green) -->
            <circle class="donut-segment" cx="20" cy="20" r="15.915" fill="transparent" stroke="var(--success)" stroke-width="4"
              :stroke-dasharray="`${chartData.approved} ${100 - chartData.approved}`" stroke-dashoffset="25"></circle>
            
            <!-- Refused Segment (Red) -->
            <circle class="donut-segment" cx="20" cy="20" r="15.915" fill="transparent" stroke="var(--error)" stroke-width="4"
              :stroke-dasharray="`${chartData.refused} ${100 - chartData.refused}`" :stroke-dashoffset="25 - chartData.approved"></circle>
              
            <!-- Pending Segment (Orange) -->
            <circle class="donut-segment" cx="20" cy="20" r="15.915" fill="transparent" stroke="var(--warning)" stroke-width="4"
              :stroke-dasharray="`${chartData.pending} ${100 - chartData.pending}`" :stroke-dashoffset="25 - chartData.approved - chartData.refused"></circle>
          </svg>
          <div class="chart-legend">
            <div class="legend-item"><span class="dot bg-success"></span> Validées ({{ approvedCount }})</div>
            <div class="legend-item"><span class="dot bg-warning"></span> En attente ({{ pendingRequests.length }})</div>
            <div class="legend-item"><span class="dot bg-error"></span> Refusées ({{ refusedCount }})</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Pending Requests Table -->
    <div class="card table-container">
      <div class="admin-header">
        <h3 class="section-title" style="margin:0">
          <i class="fas fa-tasks"></i> File de Traitement
        </h3>
        <span class="status-badge status-pending">{{ pendingRequests.length }} en attente</span>
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
              <th>Détails</th>
              <th>Période</th>
              <th>Solde Actuel</th>
              <th class="text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading">
              <td colspan="5" class="text-center py-12 text-gray-400">
                <i class="fas fa-spinner fa-spin mr-2"></i> Chargement de la file...
              </td>
            </tr>
            <tr v-else-if="filteredRequests.length === 0">
              <td colspan="5">
                <div class="empty-state">
                  <div class="success-icon-bg">
                    <i class="fas fa-check"></i>
                  </div>
                  <p>Toutes les demandes ont été traitées !</p>
                </div>
              </td>
            </tr>
            <tr v-for="req in filteredRequests" :key="req.id">
              <td data-label="Employé">
                <div class="user-info-mini">
                  <div class="user-avatar-mini">{{ getInitials(req) }}</div>
                  <div class="user-details-mini">
                    <div class="user-name-mini">{{ req.prenomEmploye }} {{ req.nomEmploye }}</div>
                    <div class="user-dept-mini">{{ req.userId }}</div>
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
                <div style="font-size:0.6875rem;font-weight:700;color:var(--primary-blue)">{{ req.dureeJoursOuvres }} Jours</div>
              </td>
              <td data-label="Solde">
                <div class="solde-remaining">
                   <small>Reste estimé</small>
                   <strong>{{ req.soldeCongesAvant - req.dureeJoursOuvres }} j</strong>
                </div>
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
    refusedCount() {
      return this.allRequests.filter(r => r.statut === 'REFUSEE').length;
    },
    absenceRate() {
      if (this.allRequests.length === 0) return '0.0';
      return ((this.approvedCount / Math.max(this.allRequests.length, 1)) * 100).toFixed(1);
    },
    chartData() {
      const total = Math.max(this.allRequests.length, 1);
      return {
        approved: (this.approvedCount / total) * 100,
        pending: (this.pendingRequests.length / total) * 100,
        refused: (this.refusedCount / total) * 100
      };
    },
    uniqueTypes() {
      const types = this.pendingRequests.map(r => r.typeConge?.libelle).filter(Boolean);
      return [...new Set(types)];
    },
    filteredRequests() {
      return this.pendingRequests.filter(r => {
        const q = this.search.toLowerCase();
        const fullName = `${r.prenomEmploye} ${r.nomEmploye}`.toLowerCase();
        const matchSearch = !q || fullName.includes(q) || r.userId.toLowerCase().includes(q);
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
      const parent = this.$root.$children[0];
      const isValider = action === 'valider';
      const fullName = `${req.prenomEmploye} ${req.nomEmploye}`;

      if (parent && parent.showConfirm) {
        const confirmed = await parent.showConfirm({
          title: isValider ? 'Valider la demande ?' : 'Refuser la demande ?',
          message: `Voulez-vous vraiment ${isValider ? 'valider' : 'refuser'} cette demande de ${fullName} ?`,
          icon: isValider ? 'fas fa-check-circle' : 'fas fa-times-circle',
          type: isValider ? 'success' : 'danger',
          confirmText: isValider ? 'Oui, valider' : 'Oui, refuser',
          btnClass: isValider ? 'btn-success' : 'btn-danger'
        });

        if (!confirmed) return;
      }

      const comment = '';

      try {
        if (isValider) {
          await apiService.validerDemande(req.id, comment);
          this.$emit('show-notification', `Demande de ${fullName} validée.`);
        } else {
          await apiService.refuserDemande(req.id, comment);
          this.$emit('show-notification', `Demande de ${fullName} refusée.`, "warning");
        }
        await this.fetchRequests();
      } catch (e) {
        let msg = "Erreur lors du traitement.";
        if (e.response && e.response.data && e.response.data.message) {
          msg = e.response.data.message;
        }
        this.$emit('show-notification', msg, "error");
      }
    },
    async viewDetail(req) {
      const parent = this.$root.$children[0];
      const fullName = `${req.prenomEmploye} ${req.nomEmploye}`;
      if (parent && parent.showConfirm) {
        await parent.showConfirm({
          title: `Détails demande #${req.numero || req.id.substring(0,8)}`,
          message: `Employé: ${fullName} (${req.userId})\nType: ${req.typeConge.libelle}\nDu ${this.formatDate(req.dateDebut)} au ${this.formatDate(req.dateFin)}\nDurée: ${req.dureeJoursOuvres} jours\nMotif: ${req.motif || 'Non spécifié'}`,
          icon: 'fas fa-info-circle',
          type: 'info',
          confirmText: 'Fermer',
          btnClass: 'btn-primary'
        });
      }
    },
    getInitials(req) {
      if (req.prenomEmploye && req.nomEmploye) {
        return (req.prenomEmploye[0] + req.nomEmploye[0]).toUpperCase();
      }
      return req.userId ? req.userId.substring(0, 2).toUpperCase() : '?';
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
