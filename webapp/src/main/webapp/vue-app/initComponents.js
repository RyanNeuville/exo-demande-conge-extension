import congeAppMain from './components/congeAppMain.vue';

/* Enregistrement du composant racine globalement dans l'instance Vue du portail */
if (typeof Vue !== 'undefined') {
  Vue.component('conge-main', congeAppMain);
  console.log('[DemandeConge] Composant <conge-main> enregistré avec succès.');
} else {
  console.error('[DemandeConge] Vue global non trouvé. L\'enregistrement a échoué.');
}