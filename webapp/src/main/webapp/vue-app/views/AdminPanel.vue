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
         <div class="flex-actions" style="display:flex; gap:0.75rem">
           <select v-model="filterType" class="select select-sm">
             <option value="ALL">Tous les types</option>
             <option v-for="t in uniqueTypes" :key="t" :value="t">{{ t }}</option>
           </select>
           <button class="btn btn-outline btn-sm" @click="exportReport" title="Générer Rapport PDF">
             <i class="fas fa-file-pdf"></i> Exporter Rapport
           </button>
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

    <!-- Admin Activity Report (Print Only) -->
    <div id="admin-print-area">
      <div v-if="printData" class="report-container">
        <header class="report-header">
          <kozao-logo width="150" height="50"></kozao-logo>
          <div class="report-title-box">
             <h1>RAPPORT D'ACTIVITÉ DES CONGÉS</h1>
             <p>Généré le {{ formatFullDate(new Date()) }}</p>
          </div>
        </header>

        <section class="report-summary">
          <div class="report-stat-box">
            <span class="label">Total Demandes</span>
            <span class="value">{{ allRequests.length }}</span>
          </div>
          <div class="report-stat-box">
            <span class="label">Validées</span>
            <span class="value">{{ approvedCount }}</span>
          </div>
          <div class="report-stat-box">
            <span class="label">En Attente</span>
            <span class="value">{{ pendingRequests.length }}</span>
          </div>
          <div class="report-stat-box">
            <span class="label">Taux d'Absence</span>
            <span class="value">{{ absenceRate }}%</span>
          </div>
        </section>

        <table class="report-table">
          <thead>
            <tr>
              <th>Réf</th>
              <th>Employé</th>
              <th>Type</th>
              <th>Période</th>
              <th>Durée</th>
              <th>Statut</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="req in allRequests" :key="'print-'+req.id">
              <td>#{{ req.id.substring(0,6) }}</td>
              <td>{{ req.prenomEmploye }} {{ req.nomEmploye }}</td>
              <td>{{ req.typeConge.libelle }}</td>
              <td>{{ formatDate(req.dateDebut) }} - {{ formatDate(req.dateFin) }}</td>
              <td>{{ req.dureeJoursOuvres }} J</td>
              <td>{{ formatStatus(req.statut) }}</td>
            </tr>
          </tbody>
        </table>

        <footer class="report-footer">
          <p>Ce document est destiné à la traçabilité interne de Kozao Africa.</p>
          </footer>
      </div>
    </div>

    <!-- Admin Detail Modal -->
    <div v-if="viewingDemande" class="modal-overlay" @click.self="closeDetailModal">
      <div class="modal-box large">
         <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h3 class="modal-title" style="margin:0"><i class="fas fa-file-invoice"></i> Détails Demande</h3>
            <span class="status-badge" :class="getBadgeClass(viewingDemande.statut)">{{ formatStatus(viewingDemande.statut) }}</span>
         </div>

         <div class="details-grid">
            <div class="detail-item">
              <label>Employé</label>
              <span>{{ viewingDemande.prenomEmploye }} {{ viewingDemande.nomEmploye }}</span>
            </div>
            <div class="detail-item">
              <label>Type de Congé</label>
              <span>{{ viewingDemande.typeConge.libelle }}</span>
            </div>
            <div class="detail-item">
              <label>Période</label>
              <span>Du {{ formatDate(viewingDemande.dateDebut) }} au {{ formatDate(viewingDemande.dateFin) }}</span>
            </div>
            <div class="detail-item">
              <label>Durée</label>
              <span>{{ viewingDemande.dureeJoursOuvres }} Jours</span>
            </div>
            <div class="detail-item">
              <label>ID Utilisateur</label>
              <span>{{ viewingDemande.userId }}</span>
            </div>
            <div class="detail-item">
              <label>Solde Estimé</label>
              <span>{{ viewingDemande.soldeCongesAvant - viewingDemande.dureeJoursOuvres }} j</span>
            </div>
         </div>

         <div class="detail-item" style="margin-bottom: 2rem; border-top: 1px solid var(--border-light); padding-top: 1.5rem;">
            <label>Motif de la demande</label>
            <p style="margin:0; font-size:0.9375rem; color:var(--text-dark); line-height: 1.6;">{{ viewingDemande.motif || 'Aucun motif spécifié' }}</p>
         </div>

         <div v-if="historyLoading" class="text-center py-4">
            <i class="fas fa-spinner fa-spin text-gray-400"></i> Chargement de l'historique...
         </div>
         <div v-else-if="auditTrail.length > 0">
            <h4 class="timeline-title">Historique des actions</h4>
            <div class="timeline" style="max-height: 200px; overflow-y: auto;">
              <div v-for="(step, idx) in auditTrail" :key="step.id || idx" class="timeline-item" :class="{active: idx === 0}">
                <span class="timeline-date">{{ formatDateTime(step.dateChangement) }}</span>
                <span class="timeline-desc">{{ formatStepAction(step) }}</span>
                <span class="timeline-user">par {{ step.acteurId }}</span>
                <span v-if="step.commentaire" class="timeline-comment">"{{ step.commentaire }}"</span>
              </div>
            </div>
         </div>

         <div class="modal-actions" style="margin-top: 2rem; border-top: 1px solid var(--border-light); padding-top: 1.5rem;">
            <button class="btn btn-outline" @click="closeDetailModal">Fermer</button>
            <div v-if="viewingDemande.statut === 'EN_ATTENTE'" style="display:flex; gap:0.5rem">
              <button class="btn btn-danger" @click="processRequest(viewingDemande, 'refuser'); closeDetailModal()">Refuser</button>
              <button class="btn btn-success" @click="processRequest(viewingDemande, 'valider'); closeDetailModal()">Valider</button>
            </div>
         </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';
import KozaoLogo from '../components/KozaoLogo.vue';

export default {
  components: { KozaoLogo },
  data: () => ({
    allRequests: [],
    pendingRequests: [],
    loading: true,
    search: '',
    filterType: 'ALL',
    
    viewingDemande: null,
    auditTrail: [],
    historyLoading: false,

    printData: null
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
      const types = this.allRequests.map(r => r.typeConge?.libelle).filter(Boolean);
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
      this.viewingDemande = req;
      this.historyLoading = true;
      this.auditTrail = [];
      try {
        const resp = await apiService.getHistoriqueDemande(req.id);
        this.auditTrail = (resp.data || []).sort((a,b) => new Date(b.dateChangement) - new Date(a.dateChangement));
      } catch (e) {
        console.error("Failed to load audit trail", e);
      } finally {
        this.historyLoading = false;
      }
    },
    closeDetailModal() {
      this.viewingDemande = null;
      this.auditTrail = [];
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
    },
    formatDateTime(dtStr) {
      if (!dtStr) return 'N/A';
      return new Date(dtStr).toLocaleString('fr-FR', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' });
    },
    formatStatus(statut) {
      const labels = { 'EN_ATTENTE': 'En attente', 'VALIDEE': 'Validée', 'REFUSEE': 'Refusée', 'ANNULEE': 'Annulée' };
      return labels[statut] || statut;
    },
    formatStepAction(step) {
      if (step.nouvelEtat === 'EN_ATTENTE') return 'Soumission';
      if (step.nouvelEtat === 'VALIDEE') return 'Validation';
      if (step.nouvelEtat === 'REFUSEE') return 'Refus';
      if (step.nouvelEtat === 'ANNULEE') return 'Annulation';
      return 'Changement d\'état';
    },
    getBadgeClass(statut) {
      return {
        'status-valid': statut === 'VALIDEE',
        'status-pending': statut === 'EN_ATTENTE',
        'status-error': statut === 'REFUSEE' || statut === 'ANNULEE'
      };
    },
    formatFullDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleDateString('fr-FR', {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    exportReport() {
      this.printData = { generatedAt: new Date() };
      this.$nextTick(() => {
        setTimeout(() => {
          window.print();
        }, 500);
      });
    }
  }
};
</script>
