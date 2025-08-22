<template>
  <div id="vue_webpack_demande_conge">
    <div class="max-w-3xl mx-auto bg-white shadow-md rounded-xl p-6 border">
      <!-- Header -->
      <div class="flex items-center space-x-2 mb-6">
        <span class="text-orange-500 text-2xl">📅</span>
        <div>
          <h2 class="text-lg font-bold">Nouvelle demande de congé</h2>
          <p class="text-sm text-gray-500">
            Créez votre demande en quelques clics
          </p>
        </div>
      </div>

      <div class="grid grid-cols-3 gap-6">
        <!-- Formulaire -->
        <div class="col-span-2 space-y-4">
          <!-- Type de congé -->
          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Type de congé</label
            >
            <select
              v-model="form.typeConge"
              class="w-full mt-1 border rounded-lg px-3 py-2 focus:ring-orange-400 focus:border-orange-400"
            >
              <option disabled value="">-- Sélectionnez --</option>
              <option>Congés payés</option>
              <option>Congé sans solde</option>
              <option>Congé maladie</option>
            </select>
          </div>

          <!-- Dates -->
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700"
                >Date de début</label
              >
              <input
                type="date"
                v-model="form.dateDebut"
                class="w-full mt-1 border rounded-lg px-3 py-2 focus:ring-orange-400 focus:border-orange-400"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700"
                >Date de fin</label
              >
              <input
                type="date"
                v-model="form.dateFin"
                class="w-full mt-1 border rounded-lg px-3 py-2 focus:ring-orange-400 focus:border-orange-400"
              />
            </div>
          </div>

          <!-- Commentaires -->
          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Commentaires</label
            >
            <textarea
              v-model="form.commentaires"
              rows="3"
              class="w-full mt-1 border rounded-lg px-3 py-2 focus:ring-orange-400 focus:border-orange-400"
            ></textarea>
          </div>

          <!-- Erreur -->
          <div
            v-if="error"
            class="bg-red-100 text-red-700 p-2 rounded-lg text-sm"
          >
            ⚠️ veuillez remplir tous les champs
          </div>

          <!-- Boutons -->
          <div class="flex justify-end space-x-3">
            <button
              type="button"
              class="px-4 py-2 rounded-lg border bg-gray-100 hover:bg-gray-200 text-gray-700"
              @click="resetForm"
            >
              Annuler
            </button>
            <button
              type="button"
              class="px-4 py-2 rounded-lg bg-orange-500 hover:bg-orange-600 text-white"
              @click="submitForm"
            >
              Soumettre la demande
            </button>
          </div>
        </div>

        <!-- Solde de congés -->
        <div class="bg-orange-50 p-4 rounded-lg border border-orange-200">
          <h3 class="text-sm font-semibold text-gray-700 flex items-center">
            📅 Solde de congés
          </h3>
          <p class="text-orange-600 font-medium mt-2">20 jours restant(s)</p>
          <p class="text-xs text-gray-500 mt-3">
            Veuillez vérifier vos informations car <br />
            aucune modification n'est possible <br />
            après la soumission de la demande.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';

const form = reactive({
  typeConge: '',
  dateDebut: '',
  dateFin: '',
  commentaires: '',
});

const error = ref(false);

function resetForm() {
  form.typeConge = '';
  form.dateDebut = '';
  form.dateFin = '';
  form.commentaires = '';
  error.value = false;
}

function submitForm() {
  if (!form.typeConge || !form.dateDebut || !form.dateFin) {
    error.value = true;
    return;
  }
  error.value = false;

  // Simulation envoi
  console.log('Demande envoyée ✅', form);
  console.log('Votre demande de congé a été soumise !');
  resetForm();
}
</script>
