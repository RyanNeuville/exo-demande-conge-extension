<template>
  <div class="admin-conge-list container mx-auto py-12 px-4">
    <h2 class="text-2xl font-bold text-blue-800 mb-6">{{ translations.adminDemandes }}</h2>
    <div class="stats shadow mb-8 grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="stat">
        <div class="stat-figure text-primary">
          <i class="fas fa-list fa-2x"></i>
        </div>
        <div class="stat-title">{{ translations.totalDemandes }}</div>
        <div class="stat-value">{{ stats.total }}</div>
      </div>
      <div class="stat">
        <div class="stat-figure text-success">
          <i class="fas fa-check-circle fa-2x"></i>
        </div>
        <div class="stat-title">{{ translations.approvedDemandes }}</div>
        <div class="stat-value">{{ stats.approved }}</div>
      </div>
      <div class="stat">
        <div class="stat-figure text-warning">
          <i class="fas fa-hourglass-half fa-2x"></i>
        </div>
        <div class="stat-title">{{ translations.pendingDemandes }}</div>
        <div class="stat-value">{{ stats.pending }}</div>
      </div>
    </div>
    <div v-for="(user, userIndex) in allDemandes" :key="userIndex">
      <h3 class="text-xl font-semibold text-blue-700 mb-4">{{ user.fullName }} ({{ user.userName }})</h3>
      <div class="overflow-x-auto">
        <table class="table w-full">
          <thead>
            <tr>
              <th>{{ translations.dateDebut }}</th>
              <th>{{ translations.dateFin }}</th>
              <th>{{ translations.typeConge }}</th>
              <th>{{ translations.motif }}</th>
              <th>{{ translations.status }}</th>
              <th>{{ translations.submittedAt }}</th>
              <th>{{ translations.actions }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(demande, index) in user.demandes" :key="index">
              <td>{{ demande.dateDebut }}</td>
              <td>{{ demande.dateFin }}</td>
              <td>{{ demande.typeConge }}</td>
              <td>{{ demande.motif }}</td>
              <td>
                <span :class="{
                  'badge badge-success': demande.status === 'APPROUVE',
                  'badge badge-warning': demande.status === 'EN_ATTENTE',
                  'badge badge-error': demande.status === 'REJETE' || demande.status === 'ANNULE'
                }">{{ demande.status }}</span>
              </td>
              <td>{{ demande.submittedAt || 'N/A' }}</td>
              <td class="space-x-2">
                <button v-if="demande.status === 'EN_ATTENTE'" class="btn btn-success btn-sm" @click="openConfirmModal('approve', user.userName, index)">{{ translations.approve }}</button>
                <button v-if="demande.status === 'EN_ATTENTE'" class="btn btn-primary btn-sm" @click="openUpdateForm(user.userName, index, demande)">{{ translations.update }}</button>
                <button v-if="demande.status === 'EN_ATTENTE'" class="btn btn-error btn-sm" @click="openConfirmModal('reject', user.userName, index)">{{ translations.reject }}</button>
                <button v-if="demande.status === 'EN_ATTENTE'" class="btn btn-secondary btn-sm" @click="openConfirmModal('cancel', user.userName, index)">{{ translations.cancel }}</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <form v-if="editing && editing.userName === user.userName && editing.index === index" @submit.prevent="updateDemande" class="mt-4 space-y-4 bg-white p-6 rounded-lg shadow-lg">
        <input type="date" v-model="editing.demande.dateDebut" class="input input-bordered w-full" required>
        <input type="date" v-model="editing.demande.dateFin" class="input input-bordered w-full" required>
        <select v-model="editing.demande.typeConge" class="select select-bordered w-full" required>
          <option value="" disabled>{{ translations.selectType }}</option>
          <option value="VACANCES">{{ translations.vacances }}</option>
          <option value="MALADIE">{{ translations.maladie }}</option>
          <option value="AUTRE">{{ translations.autre }}</option>
        </select>
        <textarea v-model="editing.demande.motif" class="textarea textarea-bordered w-full" required></textarea>
        <div class="flex space-x-4">
          <button type="submit" class="btn btn-primary">{{ translations.save }}</button>
          <button type="button" class="btn btn-secondary" @click="editing = null">{{ translations.cancelEdit }}</button>
        </div>
      </form>
    </div>
    <input type="checkbox" :id="modalId" class="modal-toggle" v-model="showModal" />
    <div class="modal">
      <div class="modal-box">
        <h3 class="font-bold text-lg">{{ modalTitle }}</h3>
        <p class="py-4">{{ modalMessage }}</p>
        <div class="modal-action">
          <button class="btn btn-primary" @click="confirmAction">{{ translations.confirm }}</button>
          <button class="btn btn-secondary" @click="showModal = false">{{ translations.cancel }}</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { translations } from '../translations.js';

export default {
  data: () => ({
    translations,
    allDemandes: [],
    editing: null,
    showModal: false,
    modalId: 'confirmModal',
    modalTitle: '',
    modalMessage: '',
    pendingAction: null,
    stats: { total: 0, approved: 0, pending: 0 }
  }),
  created() {
    this.fetchAllDemandes();
  },
  methods: {
    fetchAllDemandes() {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/all`, {
        method: 'GET',
        credentials: 'include'
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error fetching all demandes');
        }
        return resp.json();
      }).then(data => {
        this.allDemandes = data;
        this.calculateStats();
      }).catch(err => {
        this.$emit('error', this.translations.fetchError);
        console.error(err);
      });
    },
    calculateStats() {
      let total = 0, approved = 0, pending = 0;
      this.allDemandes.forEach(user => {
        total += user.demandes.length;
        user.demandes.forEach(demande => {
          if (demande.status === 'APPROUVE') approved++;
          if (demande.status === 'EN_ATTENTE') pending++;
        });
      });
      this.stats = { total, approved, pending };
    },
    openConfirmModal(action, userName, index) {
      this.pendingAction = { action, userName, index };
      this.modalTitle = this.translations[action];
      this.modalMessage = this.translations[`confirm${action.charAt(0).toUpperCase() + action.slice(1)}`];
      this.showModal = true;
    },
    confirmAction() {
      const { action, userName, index } = this.pendingAction;
      this[`${action}Demande`](userName, index);
      this.showModal = false;
    },
    approveDemande(userName, index) {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/approve`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `userName=${encodeURIComponent(userName)}&index=${index}`
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error approving demande');
        }
        this.$emit('success', this.translations.approveSuccess);
        this.fetchAllDemandes();
      }).catch(err => {
        this.$emit('error', this.translations.actionError);
        console.error(err);
      });
    },
    openUpdateForm(userName, index, demande) {
      this.editing = { userName, index, demande: { ...demande } };
    },
    updateDemande() {
      const body = `userName=${encodeURIComponent(this.editing.userName)}&index=${this.editing.index}&dateDebut=${encodeURIComponent(this.editing.demande.dateDebut)}&dateFin=${encodeURIComponent(this.editing.demande.dateFin)}&typeConge=${encodeURIComponent(this.editing.demande.typeConge)}&motif=${encodeURIComponent(this.editing.demande.motif)}`;
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/update`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error updating demande');
        }
        this.$emit('success', this.translations.updateSuccess);
        this.editing = null;
        this.fetchAllDemandes();
      }).catch(err => {
        this.$emit('error', this.translations.actionError);
        console.error(err);
      });
    },
    rejectDemande(userName, index) {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/reject`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `userName=${encodeURIComponent(userName)}&index=${index}`
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error rejecting demande');
        }
        this.$emit('success', this.translations.rejectSuccess);
        this.fetchAllDemandes();
      }).catch(err => {
        this.$emit('error', this.translations.actionError);
        console.error(err);
      });
    },
    cancelDemande(userName, index) {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/cancel`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `userName=${encodeURIComponent(userName)}&index=${index}`
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error canceling demande');
        }
        this.$emit('success', this.translations.cancelSuccess);
        this.fetchAllDemandes();
      }).catch(err => {
        this.$emit('error', this.translations.actionError);
        console.error(err);
      });
    }
  }
};
</script>