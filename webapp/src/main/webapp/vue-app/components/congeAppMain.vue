<template>
  <div id="demandeCongeApp">
    <div class="app-container">
      <!-- Sidebar Navigation -->
      <aside class="sidebar">
        <div class="sidebar-brand">
          <div class="sidebar-logo">
             <i class="fas fa-plane-departure"></i>
          </div>
          <h1>DemandeConge</h1>
        </div>

        <nav class="nav-menu">
          <router-link to="/" exact class="nav-item">
            <i class="fas fa-th-large"></i>
            Tableau de Bord
          </router-link>
          
          <router-link to="/nouvelle-demande" class="nav-item">
            <i class="fas fa-plus-circle"></i>
            Nouvelle Demande
          </router-link>

          <router-link to="/historique" class="nav-item">
            <i class="fas fa-history"></i>
            Mes Demandes
          </router-link>

          <div v-if="isAdmin" class="admin-section">
            <div class="nav-section-title">Administration</div>
            <router-link to="/administration" class="nav-item">
              <i class="fas fa-user-shield"></i>
              Gestion Équipe
            </router-link>
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
           <h2>{{ currentRouteTitle }}</h2>
           <div class="header-actions">
              <button class="btn btn-outline" @click="refreshData">
                 <i class="fas fa-sync-alt"></i>
              </button>
           </div>
        </header>

        <!-- Dynamic Views with Transitions -->
        <transition name="fade" mode="out-in">
          <router-view :key="$route.path"></router-view>
        </transition>
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

export default {
  data: () => ({
    userName: (eXo && eXo.env && eXo.env.portal && eXo.env.portal.userName) || 'Utilisateur',
    userRole: 'Employé',
    isAdmin: false,
    notification: null
  }),
  computed: {
    userInitials() {
      return this.userName.split(' ').map(n => n[0]).join('').toUpperCase().substring(0, 2);
    },
    currentRouteTitle() {
      return this.$route.meta?.title || 'Gestion des Congés';
    }
  },
  created() {
    this.checkUserRole();
  },
  methods: {
    async checkUserRole() {
       try {
         /* Appel pour vérifier si responsable/admin */
         const resp = await apiService.getDemandesATraiter();
         if (resp.status === 200) {
           this.isAdmin = true;
           this.userRole = 'Responsable';
         }
       } catch (e) {
         this.isAdmin = false;
         this.userRole = 'Employé';
       }
    },
    refreshData() {
       /* Trigger refresh logic in views if needed via event bus or shared state */
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