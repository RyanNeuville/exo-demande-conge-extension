<template>
  <div class="dashboard-view">
    <!-- Quick Stats Grid -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-icon icon-orange">
          <i class="fas fa-wallet"></i>
        </div>
        <div class="stat-info">
          <h3>{{ solde }}</h3>
          <span>Jours Restants</span>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon icon-blue">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-info">
          <h3>{{ pendingCount }}</h3>
          <span>En Attente</span>
        </div>
      </div>

      <div class="card stat-card">
        <div class="stat-icon icon-blue">
          <i class="fas fa-calendar-check"></i>
        </div>
        <div class="stat-info">
          <h3>{{ validatedYear }}</h3>
          <span>Pris ({{ currentYear }})</span>
        </div>
      </div>
    </div>

    <div class="dashboard-grid">
      <!-- Recent Activity -->
      <section class="card">
        <h3 class="section-title">
          <i class="fas fa-stream"></i>
          Activités Récentes
        </h3>
        <div v-if="loading" class="skeleton-placeholder py-4">
           Chargement en cours...
        </div>
        <div v-else-if="recentDemandes.length === 0" class="empty-state py-12 text-center text-gray-500">
           Aucune activité récente.
        </div>
        <ul v-else class="activity-list">
          <li v-for="demande in recentDemandes" :key="demande.id" class="activity-item">
            <div class="activity-main">
               <div class="status-indicator" :class="getStatusClass(demande.statut)"></div>
               <div class="activity-details">
                 <p class="activity-type">{{ demande.typeConge.libelle }}</p>
                 <small class="activity-date">Du {{ formatDate(demande.dateDebut) }} au {{ formatDate(demande.dateFin) }}</small>
               </div>
            </div>
            <span class="status-badge" :class="getBadgeClass(demande.statut)">{{ demande.statut }}</span>
          </li>
        </ul>
        <div class="mt-6">
           <router-link to="/historique" class="btn btn-outline w-full">Voir tout l'historique</router-link>
        </div>
      </section>

      <!-- Quick Actions / Tips -->
      <section class="space-y-6">
         <div class="promo-card">
            <div class="promo-content">
               <h3>Besoin de repos ?</h3>
               <p>Soumettez votre demande en quelques clics et suivez son approbation en temps réel.</p>
               <router-link to="/nouvelle-demande" class="btn btn-primary">
                 Poser un Congé
               </router-link>
            </div>
            <i class="fas fa-umbrella-beach promo-icon"></i>
         </div>

         <div class="card">
            <h3 class="section-title">Politique de l'entreprise</h3>
            <ul class="policy-list">
               <li>Délai de prévenance de 48h minimum.</li>
               <li>Validation par le responsable direct requise.</li>
               <li>Le solde est mis à jour à la soumission.</li>
            </ul>
         </div>
      </section>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';

export default {
  data: () => ({
    solde: '...',
    pendingCount: 0,
    validatedYear: 0,
    currentYear: new Date().getFullYear(),
    recentDemandes: [],
    loading: true
  }),
  created() {
    this.loadData();
  },
  methods: {
    async loadData() {
      try {
        const [soldeResp, demandesResp] = await Promise.all([
          apiService.getMonSolde(),
          apiService.getMesDemandes()
        ]);
        
        this.solde = soldeResp.data.solde;
        this.recentDemandes = demandesResp.data.slice(0, 5);
        this.pendingCount = demandesResp.data.filter(d => d.statut === 'EN_ATTENTE').length;
        this.validatedYear = demandesResp.data
          .filter(d => d.statut === 'VALIDEE' && new Date(d.dateDebut).getFullYear() === this.currentYear)
          .reduce((acc, d) => acc + d.dureeJoursOuvres, 0);
          
      } catch (e) {
        console.error("Dashboard data load error", e);
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      return new Date(dateStr).toLocaleDateString();
    },
    getStatusClass(statut) {
      return {
        'bg-green-500': statut === 'VALIDEE',
        'bg-orange-500': statut === 'EN_ATTENTE',
        'bg-red-500': statut === 'REFUSEE' || statut === 'ANNULEE',
        'bg-gray-400': statut === 'BROUILLON'
      };
    },
    getBadgeClass(statut) {
       return {
         'status-valid': statut === 'VALIDEE',
         'status-pending': statut === 'EN_ATTENTE',
         'status-error': statut === 'REFUSEE' || statut === 'ANNULEE'
       };
    }
  }
};
</script>

<style scoped>
.activity-list {
  max-height: 400px;
  overflow-y: auto;
}
</style>
