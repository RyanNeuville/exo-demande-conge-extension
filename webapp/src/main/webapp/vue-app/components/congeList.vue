<template>
  <div class="conge-list container mx-auto py-12 px-4">
    <h2 class="text-2xl font-bold text-blue-800 mb-6">{{ translations.myDemandes }}</h2>
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
          </tr>
        </thead>
        <tbody>
          <tr v-for="(demande, index) in myDemandes.demandes" :key="index">
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
            <td>{{ demande.soumisLe || 'N/A' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <h2 class="text-2xl font-bold text-blue-800 mt-12 mb-6">{{ translations.relationsDemandes }}</h2>
    <div v-for="(user, userIndex) in relationsDemandes" :key="userIndex">
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
              <td>{{ demande.soumisLe || 'N/A' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script>
import { translations } from '../translations.js';

export default {
  data: () => ({
    translations,
    myDemandes: { demandes: [] },
    relationsDemandes: []
  }),
  created() {
    this.fetchMyDemandes();
    this.fetchRelationsDemandes();
  },
  methods: {
    fetchMyDemandes() {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/my`, {
        method: 'GET',
        credentials: 'include'
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error fetching my demandes');
        }
        return resp.json();
      }).then(data => {
        this.myDemandes = data;
      }).catch(err => {
        this.$emit('error', this.translations.fetchError);
        console.error(err);
      });
    },
    fetchRelationsDemandes() {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/relations`, {
        method: 'GET',
        credentials: 'include'
      }).then(resp => {
        if (!resp.ok) {
          throw new Error('Error fetching relations demandes');
        }
        return resp.json();
      }).then(data => {
        this.relationsDemandes = data;
      }).catch(err => {
        this.$emit('error', this.translations.fetchError);
        console.error(err);
      });
    }
  }
};
</script>