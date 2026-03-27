<template>
  <div class="demande-form-view">
    <div class="form-container">
      <div class="card">
        <h3 class="section-title"><i class="fas fa-file-alt"></i> Nouvelle demande de congé</h3>

        <form @submit.prevent="submitForm" class="form-body">
          <!-- Type de Congé -->
          <div class="form-group">
            <label>Type de congé <span class="required">*</span></label>
            <select v-model="form.typeCongeId" class="select" required>
              <option value="" disabled>Sélectionnez un type</option>
              <option v-for="type in leaveTypes" :key="type.id" :value="type.id">
                {{ type.libelle }}
              </option>
            </select>
          </div>

          <!-- Dates -->
          <div class="form-row">
            <div class="form-group">
              <label>Date de début <span class="required">*</span></label>
              <input type="date" v-model="form.dateDebut" class="input" required @change="calculateDuration">
              <div class="checkbox-group">
                <input type="checkbox" id="half-start" v-model="form.demiJourneeDebut" @change="calculateDuration">
                <label for="half-start">Demi-journée</label>
              </div>
            </div>
            <div class="form-group">
              <label>Date de fin <span class="required">*</span></label>
              <input type="date" v-model="form.dateFin" class="input" required @change="calculateDuration">
              <div class="checkbox-group">
                <input type="checkbox" id="half-end" v-model="form.demiJourneeFin" @change="calculateDuration">
                <label for="half-end">Demi-journée</label>
              </div>
            </div>
          </div>

          <!-- Duration Badge -->
          <div v-if="duration !== null" class="duration-badge">
            <span class="duration-label">Durée estimée (jours ouvrés) :</span>
            <span class="duration-value">{{ duration }}</span>
          </div>

          <!-- Motif -->
          <div class="form-group">
            <label>Motif / Commentaire</label>
            <textarea v-model="form.motif" class="textarea" rows="3" placeholder="Précisez le motif si nécessaire..." required></textarea>
          </div>

          <!-- File Upload -->
          <div class="form-group">
            <label>Pièce jointe (Optionnelle)</label>
            <div class="upload-zone" @click="triggerUpload" @dragover.prevent @drop.prevent="handleDrop">
              <i class="fas fa-cloud-upload-alt"></i>
              <p>Télécharger un fichier ou <span>glisser-déposer</span></p>
              <p class="upload-hint">PDF, PNG, JPG jusqu'à 5MB</p>
            </div>
            <input type="file" ref="fileInput" style="display:none" accept=".pdf,.png,.jpg,.jpeg" @change="handleFileSelect">
            <p v-if="form.fileName" class="mt-2" style="font-size:0.8125rem;color:var(--success)">
              <i class="fas fa-paperclip"></i> {{ form.fileName }}
            </p>
          </div>

          <!-- Actions -->
          <div class="form-actions">
            <button type="button" class="btn btn-outline" @click="$emit('change-view', 'Dashboard')">Annuler</button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
              <i v-if="submitting" class="fas fa-spinner fa-spin"></i>
              <i v-else class="fas fa-paper-plane"></i>
              Soumettre la demande
            </button>
          </div>
        </form>
      </div>

      <!-- Info Note -->
      <div class="info-card">
        <div class="info-content">
          <i class="fas fa-info-circle"></i>
          <p>
            Les week-ends sont automatiquement exclus du calcul de la durée.
            Votre solde sera déduit de <strong>{{ duration || '...' }} jours</strong> après validation.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';

export default {
  data: () => ({
    form: {
      typeCongeId: '',
      dateDebut: '',
      dateFin: '',
      demiJourneeDebut: false,
      demiJourneeFin: false,
      motif: '',
      commentaireEmploye: '',
      fileName: ''
    },
    leaveTypes: [],
    duration: null,
    submitting: false
  }),
  created() {
    this.fetchTypes();
  },
  methods: {
    async fetchTypes() {
      try {
        const resp = await apiService.getTypesConges();
        this.leaveTypes = resp.data;
      } catch (e) {
        this.$emit('show-notification', "Erreur lors du chargement des types de congés", "error");
      }
    },
    triggerUpload() {
      this.$refs.fileInput.click();
    },
    handleFileSelect(e) {
      const file = e.target.files[0];
      if (file) this.form.fileName = file.name;
    },
    handleDrop(e) {
      const file = e.dataTransfer.files[0];
      if (file) this.form.fileName = file.name;
    },
    calculateDuration() {
      if (!this.form.dateDebut || !this.form.dateFin) {
        this.duration = null;
        return;
      }
      const start = new Date(this.form.dateDebut);
      const end = new Date(this.form.dateFin);
      if (end < start) { this.duration = 0; return; }

      let count = 0;
      const current = new Date(start);
      while (current <= end) {
        const day = current.getDay();
        if (day !== 0 && day !== 6) count++;
        current.setDate(current.getDate() + 1);
      }
      if (this.form.demiJourneeDebut) count -= 0.5;
      if (this.form.demiJourneeFin) count -= 0.5;
      this.duration = Math.max(0, count);
    },
    async submitForm() {
      if (this.duration <= 0) {
        this.$emit('show-notification', "La durée doit être supérieure à 0.", "warning");
        return;
      }
      this.submitting = true;
      try {
        await apiService.soumettreDemande({
          dateDebut: this.form.dateDebut,
          dateFin: this.form.dateFin,
          demiJourneeDebut: this.form.demiJourneeDebut,
          demiJourneeFin: this.form.demiJourneeFin,
          typeConge: { id: this.form.typeCongeId },
          motif: this.form.motif,
          commentaireEmploye: this.form.commentaireEmploye
        });
        this.$emit('show-notification', "Demande soumise avec succès !");
        this.$emit('change-view', 'DemandeHistory');
      } catch (e) {
        const msg = e.response?.data?.message || "Erreur lors de la soumission.";
        this.$emit('show-notification', msg, "error");
      } finally {
        this.submitting = false;
      }
    }
  }
};
</script>
