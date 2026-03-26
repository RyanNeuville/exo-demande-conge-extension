<template>
  <div class="demande-form-view">
    <div class="form-container">
      <div class="card">
        <h3 class="section-title">Nouvelle Demande de Congé</h3>
        
        <form @submit.prevent="submitForm" class="form-body">
          <!-- Type de Congé -->
          <div class="form-group">
            <label>Type de congé</label>
            <select v-model="form.typeCongeId" class="select" required>
              <option value="" disabled>Sélectionnez un type</option>
              <option v-for="type in leaveTypes" :key="type.id" :value="type.id">
                {{ type.libelle }}
              </option>
            </select>
          </div>

          <!-- Dates Selection -->
          <div class="form-row">
            <div class="form-group">
              <label>Date de début</label>
              <input type="date" v-model="form.dateDebut" class="input" required @change="calculateDuration">
              <div class="checkbox-group">
                 <input type="checkbox" id="half-start" v-model="form.demiJourneeDebut" @change="calculateDuration">
                 <label for="half-start">Demi-journée</label>
              </div>
            </div>

            <div class="form-group">
              <label>Date de fin</label>
              <input type="date" v-model="form.dateFin" class="input" required @change="calculateDuration">
              <div class="checkbox-group">
                 <input type="checkbox" id="half-end" v-model="form.demiJourneeFin" @change="calculateDuration">
                 <label for="half-end">Demi-journée</label>
              </div>
            </div>
          </div>

          <!-- Duration Summary (Live) -->
          <div v-if="duration !== null" class="duration-badge">
             <span class="duration-label">Durée estimée (jours ouvrés) :</span>
             <span class="duration-value">{{ duration }}</span>
          </div>

          <!-- Motif & Commentaire -->
          <div class="form-group">
            <label>Motif / Justification</label>
            <textarea v-model="form.motif" class="textarea" rows="3" placeholder="Ex: Vacances annuelles, Convenance personnelle..." required></textarea>
          </div>

          <div class="form-group">
            <label>Commentaire (Optionnel)</label>
            <textarea v-model="form.commentaireEmploye" class="textarea" rows="2" placeholder="Message supplémentaire pour votre manager..."></textarea>
          </div>

          <!-- Actions -->
          <div class="form-actions">
            <button type="button" class="btn btn-outline" @click="$emit('change-view', 'Dashboard')">Annuler</button>
            <button type="submit" class="btn btn-primary" :disabled="submitting">
               <i v-if="submitting" class="fas fa-spinner fa-spin"></i>
               Soumettre la demande
            </button>
          </div>
        </form>
      </div>

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
      commentaireEmploye: ''
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
        this.$root.$children[0].showNotification("Erreur lors du chargement des types de congés", "error");
      }
    },
    calculateDuration() {
      if (!this.form.dateDebut || !this.form.dateFin) {
        this.duration = null;
        return;
      }

      const start = new Date(this.form.dateDebut);
      const end = new Date(this.form.dateFin);

      if (end < start) {
        this.duration = 0;
        return;
      }

      let count = 0;
      const current = new Date(start);

      while (current <= end) {
        const day = current.getDay();
        if (day !== 0 && day !== 6) { // Exclure weekends
          count++;
        }
        current.setDate(current.getDate() + 1);
      }

      /* Ajustement pour les demi-journées */
      if (this.form.demiJourneeDebut) count -= 0.5;
      if (this.form.demiJourneeFin) count -= 0.5;

      this.duration = Math.max(0, count);
    },
    async submitForm() {
      if (this.duration <= 0) {
        this.$root.$children[0].showNotification("La durée de la demande doit être supérieure à 0.", "warning");
        return;
      }

      this.submitting = true;
      try {
        const payload = {
          dateDebut: this.form.dateDebut,
          dateFin: this.form.dateFin,
          demiJourneeDebut: this.form.demiJourneeDebut,
          demiJourneeFin: this.form.demiJourneeFin,
          typeConge: { id: this.form.typeCongeId },
          motif: this.form.motif,
          commentaireEmploye: this.form.commentaireEmploye
        };
        
        await apiService.soumettreDemande(payload);
        this.$emit('show-notification', "Demande soumise avec succès !");
        this.$emit('change-view', 'DemandeHistory');
      } catch (e) {
        const msg = e.response?.data?.message || "Erreur lors de la soumission de la demande.";
        this.$emit('show-notification', msg, "error");
      } finally {
        this.submitting = false;
      }
    }
  }
};
</script>
