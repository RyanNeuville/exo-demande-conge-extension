<template>
  <div class="dashboard-view">
    <!-- Welcome Banner -->
    <div class="welcome-banner">
      <div class="welcome-text">
        <h2>Bonjour, {{ userName }} !</h2>
        <p>Gérez vos absences et congés simplement.</p>
      </div>
      <div class="welcome-actions">
        <button class="btn btn-outline" @click="$emit('change-view', 'DemandeHistory')">
          <i class="fas fa-list"></i> Mes demandes
        </button>
        <button class="btn btn-primary" @click="$emit('change-view', 'DemandeForm')">
          <i class="fas fa-plus"></i> Nouvelle demande
        </button>
      </div>
    </div>

    <!-- Solde + Stats Row -->
    <div class="stats-row">
      <!-- Solde Card (Glassmorphism + Blue Gradient) -->
      <div class="solde-card glass-card">
        <div class="solde-label"><i class="fas fa-wallet"></i> Solde Congés Payés</div>
        <div>
          <span class="solde-number text-shadow">{{ solde }}</span>
          <span class="solde-unit">jours</span>
        </div>
        <div class="solde-meta">
          <span>Acquis : 25.0</span>
          <span>Pris : {{ validatedYear }}</span>
        </div>
      </div>

      <!-- En attente (Glass) -->
      <div class="stat-card-mini glass-card border-orange">
        <div class="stat-icon-mini orange-glow"><i class="fas fa-clock"></i></div>
        <div class="stat-number">{{ pendingCount }}</div>
        <div class="stat-label">En attente</div>
      </div>

      <!-- Validées (Glass) -->
      <div class="stat-card-mini glass-card border-green">
        <div class="stat-icon-mini green-glow"><i class="fas fa-calendar-check"></i></div>
        <div class="stat-number">{{ approvedYear }}</div>
        <div class="stat-label">Validées (2026)</div>
      </div>

      <!-- Year Countdown (New) -->
      <div class="stat-card-mini glass-card border-blue">
        <div class="stat-icon-mini blue-glow"><i class="fas fa-hourglass-half"></i></div>
        <div class="stat-number">{{ daysLeftInYear }}</div>
        <div class="stat-label">Jours restants (2026)</div>
      </div>
    </div>

    <!-- Content Grid -->
    <div class="dashboard-grid">
      <!-- Recent Activity -->
      <section class="card">
        <h3 class="section-title">
          <i class="fas fa-stream"></i>
          Activités Récentes
        </h3>

        <div v-if="loading" class="empty-state">
          <i class="fas fa-spinner fa-spin" style="font-size:1.5rem;color:var(--primary-orange)"></i>
          <p>Chargement en cours...</p>
        </div>

        <div v-else-if="recentDemandes.length === 0" class="empty-state">
          <svg width="100" height="100" viewBox="0 0 200 200" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="100" cy="100" r="80" fill="#F8FAFC"></circle>
            <path d="M60 130C60 130 80 110 100 110C120 110 140 130 140 130" stroke="#CBD5E1" stroke-width="6" stroke-linecap="round"></path>
            <circle cx="70" cy="80" r="8" fill="#CBD5E1"></circle>
            <circle cx="130" cy="80" r="8" fill="#CBD5E1"></circle>
          </svg>
          <p>Aucune activité pour le moment.<br>C'est le moment de planifier vos vacances !</p>
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
            <span class="status-badge" :class="getBadgeClass(demande.statut)">{{ formatStatus(demande.statut) }}</span>
          </li>
        </ul>

        <button v-if="recentDemandes.length > 0" class="view-all-btn" @click="$emit('change-view', 'DemandeHistory')">
          Voir tout l'historique
        </button>
      </section>

      <!-- Right Column -->
      <div class="space-y-6">
        <!-- CTA Card -->
        <div class="promo-card">
          <div class="promo-content">
            <h3>Besoin de repos ?</h3>
            <p>Soumettez votre demande en quelques clics et suivez son approbation en temps réel.</p>
            <button class="btn btn-primary" @click="$emit('change-view', 'DemandeForm')">
              <i class="fas fa-paper-plane"></i> Poser un Congé
            </button>
          </div>
          <i class="fas fa-umbrella-beach promo-icon"></i>
        </div>

        <!-- Policy Card -->
        <div class="card">
          <h3 class="section-title"><i class="fas fa-shield-alt"></i> Politique de l'entreprise</h3>
          <ul class="policy-list">
            <li>Délai de prévenance de 48h minimum.</li>
            <li>Validation par le responsable direct requise.</li>
            <li>Le solde est mis à jour à la soumission.</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';

export default {
  data: () => ({
    solde: '...',
    pendingCount: 0,
    approvedYear: 0,
    refusedCount: 0,
    validatedYear: 0,
    currentYear: new Date().getFullYear(),
    recentDemandes: [],
    loading: true,
    userName: '',
    daysLeftInYear: 0
  }),
  created() {
    this.loadData();
  },
  methods: {
    async loadData() {
      // Calcul jours restants
      const now = new Date();
      const endOfYear = new Date(now.getFullYear(), 11, 31);
      const diff = endOfYear - now;
      this.daysLeftInYear = Math.ceil(diff / (1000 * 60 * 60 * 24));

      try {
        const [soldeResp, demandesResp, userResp] = await Promise.allSettled([
          apiService.getMonSolde(),
          apiService.getMesDemandes(),
          apiService.getUtilisateurs()
        ]);

        if (soldeResp.status === 'fulfilled' && soldeResp.value.data) {
          this.solde = soldeResp.value.data.solde ?? 25;
        }

        if (userResp.status === 'fulfilled' && userResp.value.data) {
          const u = userResp.value.data;
          this.userName = `${u.prenom || ''} ${u.nom || ''}`.trim() || u.username || 'Utilisateur';
        }

        if (demandesResp.status === 'fulfilled' && demandesResp.value.data) {
          const all = demandesResp.value.data;
          this.recentDemandes = all.slice(0, 5);
          this.pendingCount = all.filter(d => d.statut === 'EN_ATTENTE').length;
          this.refusedCount = all.filter(d => d.statut === 'REFUSEE').length;
          this.approvedYear = all
            .filter(d => d.statut === 'VALIDEE' && new Date(d.dateDebut).getFullYear() === this.currentYear)
            .length;
          this.validatedYear = all
            .filter(d => d.statut === 'VALIDEE' && new Date(d.dateDebut).getFullYear() === this.currentYear)
            .reduce((acc, d) => acc + (d.dureeJoursOuvres || 0), 0);
        }
      } catch (e) {
        console.error("Dashboard data load error", e);
      } finally {
        this.loading = false;
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      const d = new Date(dateStr);
      return d.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit', year: 'numeric' });
    },
    formatStatus(statut) {
      const labels = {
        'EN_ATTENTE': 'En attente',
        'VALIDEE': 'Validée',
        'REFUSEE': 'Refusée',
        'ANNULEE': 'Annulée'
      };
      return labels[statut] || statut;
    },
    getStatusClass(statut) {
      return {
        'status-bg-green': statut === 'VALIDEE',
        'status-bg-orange': statut === 'EN_ATTENTE',
        'status-bg-red': statut === 'REFUSEE' || statut === 'ANNULEE',
        'status-bg-gray': statut === 'BROUILLON'
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
