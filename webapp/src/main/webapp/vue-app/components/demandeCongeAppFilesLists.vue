<template>
  <div id="vue_webpack_demande_conge-list">
    <div class="p-6">
      <h2 class="text-xl font-bold mb-4">📋 Liste des demandes</h2>

      <!-- Si la liste est vide -->
      <div v-if="demandes.length === 0" class="text-gray-500">
        Aucune demande pour le moment.
      </div>

      <!-- Liste des demandes -->
      <ul v-else class="space-y-3">
        <li
          v-for="demande in demandes"
          :key="demande.id"
          class="p-4 border rounded-lg shadow-sm flex justify-between items-center"
        >
          <div>
            <p class="font-semibold">{{ demande.titre }}</p>
            <p class="text-gray-600 text-sm">{{ demande.description }}</p>
            <p class="text-gray-400 text-xs">📅 {{ demande.date }}</p>
          </div>
          <span
            class="px-3 py-1 rounded-full text-white text-sm"
            :class="{
              'bg-green-500': demande.status === 'validée',
              'bg-yellow-500': demande.status === 'en attente',
              'bg-red-500': demande.status === 'rejetée',
            }"
          >
            {{ demande.status }}
          </span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DemandesList',
  data() {
    return {
      demandes: [
        {
          id: 1,
          titre: 'Demande de congé',
          description: '2 semaines en août',
          date: '2025-08-19',
          status: 'validée',
        },
        {
          id: 2,
          titre: 'Demande matériel',
          description: 'Ordinateur portable',
          date: '2025-08-18',
          status: 'en attente',
        },
        {
          id: 3,
          titre: 'Demande de remboursement',
          description: 'Frais de déplacement',
          date: '2025-08-17',
          status: 'rejetée',
        },
      ],
    };
  },
};
</script>

<style scoped></style>
