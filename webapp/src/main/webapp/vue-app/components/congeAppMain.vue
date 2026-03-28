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
/**
 * Composant Racine de l'Application de Gestion des Congés.
 * Gère la structure globale (Layout), la navigation latérale (Sidebar),
 * ainsi que les services globaux (Notifications et Boîtes de confirmation).
 * Assure la synchronisation du rôle utilisateur avec eXo Platform.
 */
import apiService from '../services/apiService';
import Dashboard from '../views/Dashboard.vue';
import DemandeForm from '../views/DemandeForm.vue';
import DemandeHistory from '../views/DemandeHistory.vue';
import AdminPanel from '../views/AdminPanel.vue';

export default {
  /**
   * Enregistrement des composants correspondant aux différentes vues.
   */
  components: { Dashboard, DemandeForm, DemandeHistory, AdminPanel },

  /**
   * État global de l'application.
   */
  data: () => ({
    currentView: 'Dashboard', // Vue actuellement affichée (Composant dynamique)
    userName: 'Utilisateur',    // Nom complet synchronisé
    userRole: 'Employé',       // Libellé du rôle pour l'affichage
    isAdmin: false,            // Flag déterminant l'accès aux fonctions d'administration
    notification: null,        // État de la notification Toast active
    confirmModal: null,        // État de la boîte de dialogue de confirmation
    loading: true             // État de chargement initial de l'application
  }),

  computed: {
    /**
     * Génère les initiales à partir du nom d'utilisateur pour l'avatar.
     */
    userInitials() {
      if (!this.userName) return 'U';
      return this.userName.split(' ').filter(Boolean).map(n => n[0]).join('').toUpperCase().substring(0, 2);
    }
  },

  /**
   * Montage : Vérification des droits d'accès dès le chargement.
   */
  async mounted() {
    await this.checkUserRole();
  },

  methods: {
    /**
     * Synchronise le profil utilisateur actuel avec le backend.
     * Détermine si l'utilisateur a des droits de Responsable ou d'Administrateur
     * pour débloquer les menus correspondants.
     */
    async checkUserRole() {
      this.loading = true;
      // Sécurité : Timeout pour éviter de bloquer l'App si eXo est lent
      const timeout = setTimeout(() => { if (this.loading) this.loading = false; }, 8000);

      try {
        const resp = await apiService.getUtilisateurs();
        if (resp.data) {
          const user = resp.data;
          this.userName = `${user.prenom || ''} ${user.nom || ''}`.trim() || user.username || 'Utilisateur';

          const role = (user.role || '').toUpperCase();
          // Logique de droits : root ou groupe admin => Administrateur. Managers => Responsable.
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
        }
      } catch (e) {
        console.error('[DemandeConge] Erreur profil:', e);
      } finally {
        clearTimeout(timeout);
        this.loading = false;
      }
    },

    /**
     * Service global de notification (Toast).
     * Peut être déclenché depuis n'importe quelle vue via @show-notification.
     * @param {string} message - Texte à afficher.
     * @param {string} type - 'success', 'error' ou 'warning'.
     */
    showNotification(message, type = 'success') {
      const icons = { success: 'fas fa-check-circle', error: 'fas fa-exclamation-circle', warning: 'fas fa-exclamation-triangle' };
      this.notification = { message, type, icon: icons[type] || icons.success };
      setTimeout(() => (this.notification = null), 4000);
    },

    /**
     * Service global de confirmation (Modal).
     * Accessible via l'instance racine ($root) pour les actions critiques.
     * Retourne une Promise résolue en true (confirmer) ou false (annuler).
     */
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