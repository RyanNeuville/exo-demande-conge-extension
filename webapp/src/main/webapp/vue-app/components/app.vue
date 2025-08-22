<template>
  <div>
    <div v-if="loading" class="loader-container">
      <div class="loader"></div>
      <p>Chargement en cours...</p>
    </div>

    <component v-else :is="currentView" />
  </div>
</template>

<script>
import DemandeCongeLogin from './demandeCongeAppMain.vue';
import DemandeCongeFilesList from './demandeCongeAppFilesLists.vue';

export default {
  name: 'DemandeCongeAppRoot',
  components: {
    DemandeCongeLogin,
    DemandeCongeFilesList,
  },
  data() {
    return {
      currentView: null,
      loading: true,
    };
  },
  methods: {

  }

};
</script>

<style scoped>
.loader-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
}

.loader {
  border: 6px solid #f3f3f3;
  border-top: 6px solid #2c9f45;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>