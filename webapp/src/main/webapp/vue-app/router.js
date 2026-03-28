/**
 * Configuration du Routeur Vue.
 * 
 * NOTE CRITIQUE : Nous utilisons le mode 'abstract' pour la navigation.
 * En tant qu'extension eXo Platform (Portlet), l'application ne doit pas
 * manipuler directement l'URL du navigateur pour ne pas interférer avec le portail.
 * La navigation est gérée en mémoire par Vue.
 */
import Vue from 'vue';
import VueRouter from 'vue-router';

/* 
 * Chargement Dynamique (Lazy Loading).
 * Les composants ne sont chargés que lorsque l'utilisateur accède à la vue,
 * ce qui réduit le temps de chargement initial de la portlet.
 */
const Dashboard = () => import('./views/Dashboard.vue');
const DemandeForm = () => import('./views/DemandeForm.vue');
const DemandeHistory = () => import('./views/DemandeHistory.vue');
const AdminPanel = () => import('./views/AdminPanel.vue');

Vue.use(VueRouter);

/**
 * Définition des routes applicatives internes.
 */
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
  mode: 'abstract', 
  routes
});

export default router;
