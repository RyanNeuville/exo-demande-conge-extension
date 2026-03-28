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
                <button v-if="demande.statut === 'EN_ATTENTE'" class="btn btn-icon btn-approve" style="color:var(--warning)" title="Modifier" @click="openEditModal(demande)">
                  <i class="fas fa-pen"></i>
                </button>
                <button v-if="demande.statut === 'EN_ATTENTE'" class="btn btn-icon btn-reject" title="Annuler" @click="cancelDemande(demande.id)">
                  <i class="fas fa-times"></i>
                </button>
                <button class="btn btn-icon btn-view" title="Détails" @click="openDetailModal(demande)">
                  <i class="fas fa-eye"></i>
                </button>
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

    <!-- Edit Modal -->
    <div v-if="editingDemande" class="modal-overlay" @click.self="closeEditModal">
      <div class="modal-box" style="width: 95%; max-width: 500px; text-align: left;">
        <h3 class="modal-title mb-4"><i class="fas fa-pen"></i> Modifier la demande</h3>
        
        <div class="form-group">
          <label>Motif / Commentaire</label>
          <textarea v-model="editForm.motif" class="textarea" rows="3" required></textarea>
        </div>

        <div class="info-card mt-4 mb-4" style="padding: 1rem;">
          <p style="margin: 0; font-size: 0.8125rem; color: #9A4A28;">
            <i class="fas fa-info-circle"></i> Seul le motif de la demande peut être modifié pour l'instant via l'interface simplifiée.
          </p>
        </div>

        <div class="modal-actions" style="justify-content: flex-end; margin-top: 1rem;">
          <button class="btn btn-outline" @click="closeEditModal">Annuler</button>
          <button class="btn btn-primary" :disabled="submittingEdit" @click="submitEdit">
            <i v-if="submittingEdit" class="fas fa-spinner fa-spin"></i>
            <i v-else class="fas fa-save"></i>
            Enregistrer
          </button>
        </div>
      </div>
    </div>

    <!-- Premium Detail Modal -->
    <div v-if="viewingDemande" class="modal-overlay" @click.self="closeDetailModal">
      <div class="modal-box large">
         <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <h3 class="modal-title" style="margin:0"><i class="fas fa-file-alt"></i> Détails de la Demande</h3>
            <span class="status-badge" :class="getBadgeClass(viewingDemande.statut)">{{ formatStatus(viewingDemande.statut) }}</span>
         </div>

         <div class="details-grid">
            <div class="detail-item">
              <label>ID de Référence</label>
              <span>#{{ viewingDemande.id.substring(0,8).toUpperCase() }}</span>
            </div>
            <div class="detail-item">
              <label>Type de Congé</label>
              <span>{{ getTypeName(viewingDemande) }}</span>
            </div>
            <div class="detail-item">
              <label>Date de Début</label>
              <span>{{ formatDate(viewingDemande.dateDebut) }}</span>
            </div>
            <div class="detail-item">
              <label>Date de Fin</label>
              <span>{{ formatDate(viewingDemande.dateFin) }}</span>
            </div>
            <div class="detail-item">
              <label>Durée Totale</label>
              <span>{{ viewingDemande.dureeJoursOuvres }} Jours ouvrés</span>
            </div>
            <div class="detail-item">
              <label>Date Soumission</label>
              <span>{{ formatDate(viewingDemande.dateSoumission) }}</span>
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
            <div class="timeline">
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
            <button v-if="viewingDemande.statut === 'VALIDEE'" class="btn btn-primary" @click="printReceipt(viewingDemande)">
              <i class="fas fa-print"></i> Télécharger Reçu
            </button>
            <button v-if="viewingDemande.statut === 'EN_ATTENTE'" class="btn btn-primary" @click="openEditModal(viewingDemande); closeDetailModal()">
              <i class="fas fa-pen"></i> Modifier
            </button>
         </div>
      </div>
    </div>

    <!-- Hidden Printable Receipt -->
    <div id="print-area">
      <div v-if="printData" class="receipt-container">
        <div class="receipt-header">
          <kozao-logo width="150" height="40" class="receipt-logo"></kozao-logo>
          <div class="receipt-title">
            <h1>REÇU DE DEMANDE DE CONGÉ</h1>
            <p>Référence : #{{ printData.id.substring(0,8).toUpperCase() }}</p>
          </div>
        </div>
        
        <div class="receipt-body">
          <div class="receipt-section">
            <h3>INFORMATIONS EMPLOYÉ</h3>
            <p><strong>Nom :</strong> {{ printData.prenomEmploye }} {{ printData.nomEmploye }}</p>
            <p><strong>Matricule/ID :</strong> {{ printData.userId }}</p>
            <p><strong>Date de demande :</strong> {{ formatDate(printData.dateSoumission) }}</p>
          </div>

          <div class="receipt-section">
            <h3>DÉTAILS DU CONGÉ</h3>
            <table class="receipt-table">
              <tr><td>Type :</td><td>{{ getTypeName(printData) }}</td></tr>
              <tr><td>Période :</td><td>Du {{ formatDate(printData.dateDebut) }} au {{ formatDate(printData.dateFin) }}</td></tr>
              <tr><td>Durée :</td><td>{{ printData.dureeJoursOuvres }} Jours ouvrés</td></tr>
              <tr><td>Motif :</td><td>{{ printData.motif || '—' }}</td></tr>
            </table>
          </div>

          <div class="receipt-status">
            <div class="status-stamp" :class="printData.statut">
              {{ formatStatus(printData.statut).toUpperCase() }}
            </div>
            <p v-if="printData.dateValidation">Validé officiellement le {{ formatDate(printData.dateValidation) }}</p>
            <p v-else>Document généré le {{ formatDateTime(new Date()) }}</p>
          </div>
        </div>

        <div class="receipt-footer">
          <p>Ce document est généré numériquement et sert de preuve de validation au sein de Kozao Africa.</p>
          <p>© {{ currentYear }} Kozao Africa - Système de Gestion des Congés</p>
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
    demandes: [],
    typesMap: {},
    search: '',
    filterStatus: 'ALL',
    loading: true,
    currentPage: 1,
    perPage: 8,
    currentYear: new Date().getFullYear(),
    
    editingDemande: null,
    editForm: { motif: '' },
    submittingEdit: false,

    viewingDemande: null,
    auditTrail: [],
    historyLoading: false,

    printData: null,
    userName: ''
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
  async created() {
    await this.fetchTypes();
    await this.fetchDemandes();
  },
  methods: {
    async fetchTypes() {
      try {
        const resp = await apiService.getTypesConges();
        const types = resp.data || [];
        types.forEach(t => {
          this.typesMap[t.id] = t.libelle;
        });
      } catch (e) {
        console.warn("Could not fetch types map", e);
      }
    },
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
        await this.fetchDemandes();
      } catch (e) {
        let msg = "Erreur lors de l'annulation.";
        if (e.response && e.response.data && e.response.data.message) {
          msg = e.response.data.message;
        }
        this.$emit('show-notification', msg, "error");
      }
    },
    async openDetailModal(req) {
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
    openEditModal(demande) {
      this.editingDemande = demande;
      this.editForm.motif = demande.motif || '';
    },
    closeEditModal() {
      this.editingDemande = null;
      this.editForm.motif = '';
    },
    async submitEdit() {
      if (!this.editingDemande) return;
      this.submittingEdit = true;
      try {
        const payload = {
          ...this.editingDemande,
          motif: this.editForm.motif
        };
        // USE PUT ENDPOINT !
        await apiService.modifierDemande(this.editingDemande.id, payload);
        
        this.$emit('show-notification', "Demande modifiée avec succès.");
        this.closeEditModal();
        await this.fetchDemandes();
      } catch (e) {
        let msg = "Erreur lors de la modification.";
        if (e.response && e.response.data && e.response.data.message) {
          msg = e.response.data.message;
        }
        this.$emit('show-notification', msg, "error");
      } finally {
        this.submittingEdit = false;
      }
    },
    getTypeName(demande) {
      if (demande.typeConge && demande.typeConge.id && this.typesMap[demande.typeConge.id]) {
        return this.typesMap[demande.typeConge.id];
      }
      if (demande.typeConge && demande.typeConge.libelle) {
        return demande.typeConge.libelle;
      }
      return 'Congé';
    },
    formatDate(dateStr) {
      if (!dateStr) return 'N/A';
      return new Date(dateStr).toLocaleDateString('fr-FR', { day: '2-digit', month: 'short', year: 'numeric' });
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
      if (step.nouvelEtat === 'EN_ATTENTE') return 'Demande soumise';
      if (step.nouvelEtat === 'VALIDEE') return 'Demande approuvée';
      if (step.nouvelEtat === 'REFUSEE') return 'Demande refusée';
      if (step.nouvelEtat === 'ANNULEE') return 'Demande annulée';
      return 'Changement d\'état';
    },
    getBadgeClass(statut) {
      return {
        'status-valid': statut === 'VALIDEE',
        'status-pending': statut === 'EN_ATTENTE',
        'status-error': statut === 'REFUSEE' || statut === 'ANNULEE'
      };
    },
    printReceipt(req) {
      this.printData = req;
      this.$nextTick(() => {
        // Small delay to ensure hidden DOM is populated before printing
        setTimeout(() => {
          window.print();
        }, 300);
      });
    }
  }
};
</script>
