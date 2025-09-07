<template>
  <div class="features container mx-auto py-12 px-4">
    <h2 class="text-3xl font-bold text-center text-blue-800 mb-8">{{ translations.featuresTitle }}</h2>
    <div class="steps steps-vertical md:steps-horizontal mb-12">
      <div class="step step-primary">{{ translations.feature1Title }}</div>
      <div class="step step-primary">{{ translations.feature2Title }}</div>
      <div class="step step-primary">{{ translations.feature3Title }}</div>
      <div v-if="isAdmin" class="step step-primary">{{ translations.feature4Title }}</div>
    </div>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div class="card bg-white shadow-lg">
        <div class="card-body">
          <h3 class="card-title">{{ translations.feature1Title }}</h3>
          <p>{{ translations.feature1Desc }}</p>
        </div>
      </div>
      <div class="card bg-white shadow-lg">
        <div class="card-body">
          <h3 class="card-title">{{ translations.feature2Title }}</h3>
          <p>{{ translations.feature2Desc }}</p>
        </div>
      </div>
      <div class="card bg-white shadow-lg">
        <div class="card-body">
          <h3 class="card-title">{{ translations.feature3Title }}</h3>
          <p>{{ translations.feature3Desc }}</p>
        </div>
      </div>
      <div v-if="isAdmin" class="card bg-white shadow-lg">
        <div class="card-body">
          <h3 class="card-title">{{ translations.feature4Title }}</h3>
          <p>{{ translations.feature4Desc }}</p>
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
    isAdmin: false
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
    }
  }
};
</script>