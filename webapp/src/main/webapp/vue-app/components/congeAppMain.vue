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
          <div class="sidebar-logo"><i class="fas fa-plane-departure"></i></div>
          <h1>Congés</h1>
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

    <!-- Toast Notification -->
    <div v-if="notification" class="toast-container" :class="notification.type">
      <i :class="notification.icon"></i>
      {{ notification.message }}
    </div>

    <!-- Confirm Modal -->
    <div v-if="confirmModal" class="modal-overlay" @click.self="confirmModal.onCancel">
      <div class="modal-box">
        <div class="modal-icon" :class="confirmModal.type || 'warning'">
          <i :class="confirmModal.icon || 'fas fa-exclamation-triangle'"></i>
        </div>
        <h3 class="modal-title">{{ confirmModal.title }}</h3>
        <p class="modal-message">{{ confirmModal.message }}</p>
        <div class="modal-actions">
          <button class="btn btn-outline" @click="confirmModal.onCancel">Annuler</button>
          <button class="btn" :class="confirmModal.btnClass || 'btn-primary'" @click="confirmModal.onConfirm">
            {{ confirmModal.confirmText || 'Confirmer' }}
          </button>
        </div>
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
  components: { Dashboard, DemandeForm, DemandeHistory, AdminPanel },
  data: () => ({
    currentView: 'Dashboard',
    userName: 'Utilisateur',
    userRole: 'Employé',
    isAdmin: false,
    notification: null,
    confirmModal: null,
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
          console.log('[DemandeConge] Profil reçu. Username:', user.username, 'Role:', user.role);
          this.userName = `${user.prenom || ''} ${user.nom || ''}`.trim() || user.username || 'Utilisateur';

          const role = (user.role || '').toUpperCase();
          if (role === 'ADMINISTRATEUR' || role === 'ADMIN' || user.username === 'root') {
            this.isAdmin = true;
            this.userRole = 'Administrateur';
          } else if (role === 'RESPONSABLE' || role === 'MANAGER') {
            this.isAdmin = true;
            this.userRole = 'Responsable';
          } else {
            this.isAdmin = false;
            this.userRole = 'Employé';
          }
          console.log('[DemandeConge] isAdmin =', this.isAdmin, 'userRole =', this.userRole);
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
    },
    showConfirm(options) {
      return new Promise((resolve) => {
        this.confirmModal = {
          title: options.title || 'Confirmation',
          message: options.message || 'Êtes-vous sûr ?',
          icon: options.icon || 'fas fa-exclamation-triangle',
          type: options.type || 'warning',
          confirmText: options.confirmText || 'Confirmer',
          btnClass: options.btnClass || 'btn-primary',
          onConfirm: () => { this.confirmModal = null; resolve(true); },
          onCancel: () => { this.confirmModal = null; resolve(false); }
        };
      });
    }
  }
};
</script>