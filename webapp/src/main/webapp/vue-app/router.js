import Vue from 'vue';
import VueRouter from 'vue-router';

/* Import des vues (à créer) */
/* Note: On utilise le chargement dynamique pour les performances */
const Dashboard = () => import('./views/Dashboard.vue');
const DemandeForm = () => import('./views/DemandeForm.vue');
const DemandeHistory = () => import('./views/DemandeHistory.vue');
const AdminPanel = () => import('./views/AdminPanel.vue');

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: Dashboard,
    meta: { title: 'Tableau de Bord' }
  },
  {
    path: '/nouvelle-demande',
    name: 'new-request',
    component: DemandeForm,
    meta: { title: 'Nouvelle Demande' }
  },
  {
    path: '/historique',
    name: 'history',
    component: DemandeHistory,
    meta: { title: 'Mes Demandes' }
  },
  {
    path: '/administration',
    name: 'admin',
    component: AdminPanel,
    meta: { title: 'Administration' }
  }
];

const router = new VueRouter({
  mode: 'abstract', /* Requis pour l'intégration en Portlet si on ne veut pas interférer avec l'URL du portail */
  routes
});

export default router;
