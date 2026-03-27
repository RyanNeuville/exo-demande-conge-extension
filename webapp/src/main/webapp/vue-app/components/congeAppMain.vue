<template>
  <div id="demandeCongeApp">
    <!-- Loading -->
    <div v-if="loading" class="app-loader">
      <div class="loader"></div>
      <p>Initialisation de votre espace de gestion des congés...</p>
    </div>

    <div v-else class="app-container">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-brand">
          <div class="sidebar-logo">K</div>
          <h1>Kozao Africa</h1>
        </div>

        <nav class="nav-menu">
          <a href="#" class="nav-item" :class="{ active: currentView === 'Dashboard' }" @click.prevent="currentView = 'Dashboard'">
            <i class="fas fa-th-large"></i> Tableau de Bord
          </a>
          <a href="#" class="nav-item" :class="{ active: currentView === 'DemandeForm' }" @click.prevent="currentView = 'DemandeForm'">
            <i class="fas fa-plus-circle"></i> Nouvelle Demande
          </a>
          <a href="#" class="nav-item" :class="{ active: currentView === 'DemandeHistory' }" @click.prevent="currentView = 'DemandeHistory'">
            <i class="fas fa-history"></i> Mes Demandes
          </a>

          <div v-if="isAdmin" class="admin-section">
            <div class="nav-section-title">Administration</div>
            <a href="#" class="nav-item" :class="{ active: currentView === 'AdminPanel' }" @click.prevent="currentView = 'AdminPanel'">
              <i class="fas fa-user-shield"></i> Gestion Équipe
            </a>
          </div>
        </nav>

        <div class="sidebar-footer">
          <div class="user-block">
            <div class="user-avatar">{{ userInitials }}</div>
            <div class="user-info">
              <p class="user-name">{{ userName }}</p>
              <p class="user-role">{{ userRole }}</p>
            </div>
          </div>
        </div>
      </aside>

      <!-- Main -->
      <main class="main-content">
        <component :is="currentView" @change-view="currentView = $event" @show-notification="showNotification" />
      </main>
    </div>

    <!-- Toast -->
    <div v-if="notification" class="toast-container" :class="notification.type">
      <i :class="notification.icon"></i>
      {{ notification.message }}
    </div>
  </div>
</template>

<script>
import apiService from '../services/apiService';
import Dashboard from '../views/Dashboard.vue';
import DemandeForm from '../views/DemandeForm.vue';
import DemandeHistory from '../views/DemandeHistory.vue';
import AdminPanel from '../views/AdminPanel.vue';

export default {
  components: { Dashboard, DemandeForm, DemandeHistory, AdminPanel },
  data: () => ({
    currentView: 'Dashboard',
    userName: 'Utilisateur',
    userRole: 'Employé',
    isAdmin: false,
    notification: null,
    loading: true
  }),
  computed: {
    userInitials() {
      if (!this.userName) return 'U';
      return this.userName.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
    }
  },
  async mounted() {
    await this.checkUserRole();
  },
  methods: {
    async checkUserRole() {
      this.loading = true;
      const timeout = setTimeout(() => { if (this.loading) this.loading = false; }, 8000);

      try {
        const resp = await apiService.getUtilisateurs();
        if (resp.data) {
          const user = resp.data;
          this.userName = `${user.prenom || ''} ${user.nom || ''}`.trim() || user.username || 'Utilisateur';
          if (user.role === 'ADMINISTRATEUR' || user.role === 'RESPONSABLE') {
            this.isAdmin = true;
            this.userRole = user.role === 'ADMINISTRATEUR' ? 'Administrateur' : 'Responsable';
          } else {
            this.isAdmin = false;
            this.userRole = 'Employé';
          }
        }
      } catch (e) {
        console.error('[DemandeConge] Erreur profil:', e);
      } finally {
        clearTimeout(timeout);
        this.loading = false;
      }
    },
    showNotification(message, type = 'success') {
      const icons = { success: 'fas fa-check-circle', error: 'fas fa-exclamation-circle', warning: 'fas fa-exclamation-triangle' };
      this.notification = { message, type, icon: icons[type] || icons.success };
      setTimeout(() => (this.notification = null), 4000);
    }
  }
};
</script>