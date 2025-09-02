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

</style>