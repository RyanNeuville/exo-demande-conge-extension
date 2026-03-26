<template>
  <div id="demandeCongeApp">
    <!-- Initial Loading Overlay -->
    <div v-if="loading" class="app-loader">
       <div class="loader"></div>
       <p>Initialisation de votre espace de gestion des congés...</p>
    </div>

    <div v-else class="app-container">
      <!-- Sidebar Navigation -->
      <aside class="sidebar">
        <div class="sidebar-brand">
          <div class="sidebar-logo">
             <i class="fas fa-plane-departure"></i>
          </div>
          <h1>DemandeConge</h1>
        </div>

        <nav class="nav-menu">
          <a href="#" class="nav-item" :class="{ active: currentView === 'Dashboard' }" @click.prevent="currentView = 'Dashboard'">
            <i class="fas fa-th-large"></i>
            Tableau de Bord
          </a>
          
          <a href="#" class="nav-item" :class="{ active: currentView === 'DemandeForm' }" @click.prevent="currentView = 'DemandeForm'">
            <i class="fas fa-plus-circle"></i>
            Nouvelle Demande
          </a>

          <a href="#" class="nav-item" :class="{ active: currentView === 'DemandeHistory' }" @click.prevent="currentView = 'DemandeHistory'">
            <i class="fas fa-history"></i>
            Mes Demandes
          </a>

          <div v-if="isAdmin" class="admin-section">
            <div class="nav-section-title">Administration</div>
            <a href="#" class="nav-item" :class="{ active: currentView === 'AdminPanel' }" @click.prevent="currentView = 'AdminPanel'">
              <i class="fas fa-user-shield"></i>
              Gestion Équipe
            </a>
          </div>
        </nav>

        <div class="sidebar-footer">
           <div class="user-block">
             <div class="user-avatar">
               {{ userInitials }}
             </div>
             <div class="user-info">
               <p class="user-name">{{ userName }}</p>
               <p class="user-role">{{ userRole }}</p>
             </div>
           </div>
        </div>
      </aside>

      <!-- Main Content Area -->
      <main class="main-content">
        <header class="page-header">
           <h2>{{ viewTitle }}</h2>
           <div class="header-actions">
              <button class="btn btn-outline" @click="refreshData">
                 <i class="fas fa-sync-alt"></i>
              </button>
           </div>
        </header>

        <!-- Dynamic Content Switcher -->
        <section class="view-content">
          <component :is="currentView" @change-view="currentView = $event" @show-notification="showNotification" />
        </section>
      </main>
    </div>

    <!-- Global Notifications (Toasts) -->
    <div v-if="notification" class="toast-container" :class="notification.type">
       <div class="toast-content">
          <i :class="notification.icon" class="mr-2"></i>
          {{ notification.message }}
       </div>
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
  components: {
    Dashboard,
    DemandeForm,
    DemandeHistory,
    AdminPanel
  },
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
      if (!this.userName) {
        return 'U';
      }
      return this.userName.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
    },
    viewTitle() {
      const titles = {
        'Dashboard': 'Tableau de Bord',
        'DemandeForm': 'Nouvelle Demande de Congé',
        'DemandeHistory': 'Historique des Demandes',
        'AdminPanel': 'Portail d\'Administration'
      };
      return titles[this.currentView] || 'Gestion des Congés';
    }
  },
  async mounted() {
    await this.checkUserRole();
  },
  methods: {
    async checkUserRole() {
      console.log('[DemandeConge] Initialisation du profil utilisateur...');
      this.loading = true;

      // Sécurité : Si l'API met plus de 8 secondes, on affiche quand même l'app
      const timeoutToken = setTimeout(() => {
        if (this.loading) {
          console.warn('[DemandeConge] Délai d\'attente dépassé pour le profil. Affichage forcé.');
          this.loading = false;
        }
      }, 8000);

      try {
        const resp = await apiService.getUtilisateurs();
        if (resp.data) {
          const user = resp.data;
          console.log('[DemandeConge] Profil reçu:', user.username, 'Rôle:', user.role);
          this.userName = `${user.prenom || ''} ${user.nom || ''}`.trim() || user.username || 'Utilisateur';
          
          if (user.role === 'ADMINISTRATEUR') {
            this.isAdmin = true;
            this.userRole = 'Administrateur';
          } else if (user.role === 'RESPONSABLE') {
            this.isAdmin = true;
            this.userRole = 'Responsable';
          } else {
            this.isAdmin = false;
            this.userRole = 'Employé';
          }
        }
      } catch (e) {
        console.error('[DemandeConge] Erreur récupération profil:', e);
        this.isAdmin = false;
        this.userRole = 'Employé';
      } finally {
        clearTimeout(timeoutToken);
        this.loading = false;
        console.log('[DemandeConge] Chargement initial terminé.');
      }
    },
    refreshData() {
       location.reload(); 
    },
    showNotification(message, type = 'success') {
      const icons = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle'
      };
      this.notification = { message, type, icon: icons[type] };
      setTimeout(() => (this.notification = null), 4000);
    }
  }
};
</script>

<style scoped>
.nav-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #CBD5E1;
  text-decoration: none;
  border-radius: 8px;
  transition: var(--transition-fast);
  font-weight: 500;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.router-link-active {
  background: var(--primary-orange) !important;
  color: white;
  box-shadow: var(--shadow-md);
}

.toast-container {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  background: white;
  box-shadow: var(--shadow-lg);
  display: flex;
  align-items: center;
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

.toast-container.success { border-left: 4px solid var(--success-color); color: var(--success-color); }
.toast-container.error { border-left: 4px solid var(--error-color); color: var(--error-color); }

@keyframes slideIn {
  from { transform: translateX(100%); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}
</style>