<template>
  <form @submit.prevent="submitDemande" class="space-y-4 bg-white p-6 rounded-lg shadow-lg">
    <input type="date" v-model="demande.dateDebut" :placeholder="translations.dateDebut" class="input input-bordered w-full" required>
    <input type="date" v-model="demande.dateFin" :placeholder="translations.dateFin" class="input input-bordered w-full" required>
    <select v-model="demande.typeConge" class="select select-bordered w-full" required>
      <option value="" disabled>{{ translations.selectType }}</option>
      <option value="VACANCES">{{ translations.vacances }}</option>
      <option value="MALADIE">{{ translations.maladie }}</option>
      <option value="AUTRE">{{ translations.autre }}</option>
    </select>
    <textarea v-model="demande.motif" :placeholder="translations.motif" class="textarea textarea-bordered w-full" required></textarea>
    <button type="submit" class="btn btn-primary w-full">{{ translations.submit }}</button>
  </form>
</template>
<script>
import { translations } from '../translations.js';

export default {
  data: () => ({
    translations,
    demande: {
      dateDebut: '',
      dateFin: '',
      typeConge: '',
      motif: ''
    }
  }),
  methods: {
    submitDemande() {
      const body = `dateDebut=${encodeURIComponent(this.demande.dateDebut)}&dateFin=${encodeURIComponent(this.demande.dateFin)}&typeConge=${encodeURIComponent(this.demande.typeConge)}&motif=${encodeURIComponent(this.demande.motif)}`;
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/submit`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error submitting demande');
        }
        this.$emit('success', this.translations.submitSuccess);
        this.demande = { dateDebut: '', dateFin: '', typeConge: '', motif: '' };
      }).catch(err => {
        this.$emit('error', this.translations.submitError);
        console.error(err);
      });
    }
  }
};
</script>