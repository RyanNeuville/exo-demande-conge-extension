<template>
  <div id="congeAppMain">
    <div class="hero bg-gradient-to-r from-blue-700 to-blue-900 text-white py-12 px-4 md:px-8">
      <div class="container mx-auto flex flex-col md:flex-row items-center justify-between">
        <div class="hero-left mb-8 md:mb-0 md:w-1/2">
          <h1 class="text-4xl font-bold">{{ translations.welcome }}, {{ userName }} !</h1>
          <p class="mt-4 text-lg">{{ translations.welcomeMessage }}</p>
        </div>
        <div class="hero-right md:w-1/2">
          <conge-form @success="showSuccess" @error="showError" />
        </div>
      </div>
    </div>
    <div class="features py-12 px-4">
      <conge-features />
    </div>
    <div class="conge-list py-12 px-4">
      <conge-list @error="showError" />
    </div>
    <div v-if="isAdmin" class="admin-conge-list py-12 px-4">
      <conge-admin-list @success="showSuccess" @error="showError" />
    </div>
    <div v-if="successMessage" class="alert alert-success">{{ successMessage }}</div>
    <div v-if="errorMessage" class="alert alert-error">{{ errorMessage }}</div>
  </div>
</template>
<script>
import { translations } from '../translations.js';

export default {
  data: () => ({
    translations,
    userName: eXo.env.portal.userName || 'Utilisateur',
    isAdmin: false,
    successMessage: '',
    errorMessage: ''
  }),
  created() {
    this.checkAdmin();
  },
  methods: {
    checkAdmin() {
      fetch(`${eXo.env.portal.context}/${eXo.env.portal.rest}/conges/enattente`, {
        method: 'GET',
        credentials: 'include'
      }).then(resp => {
        if (resp.ok) {
          this.isAdmin = true;
        }
      }).catch(() => {
        this.isAdmin = false;
      });
    },
    showSuccess(message) {
      this.successMessage = message;
      setTimeout(() => (this.successMessage = ''), 5000);
    },
    showError(message) {
      this.errorMessage = message;
      setTimeout(() => (this.errorMessage = ''), 5000);
    }
  }
};
</script>