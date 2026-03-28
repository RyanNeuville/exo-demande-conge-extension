/**
 * Point d'Entrée Principal de l'Application Vue.
 * 
 * Cette application suit le "Standard eXo Pattern" pour les portlets :
 * - Chargement asynchrone des bundles de langue (i18n).
 * - Initialisation via une fonction exportée pour la compatibilité AMD/Portal.
 * - Montage sur un ID spécifique injecté par le portail.
 */
import './initComponents.js';
import './../css/demandeCongeApp.css';

/* Configuration i18n dynamique basée sur l'environnement eXo */
const lang = eXo?.env?.portal?.language || 'fr';
const resourceBundleName = 'locale.portlet.demandeCongeApp';
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;
const appId = 'demandeCongeApp';

/**
 * Fonction d'initialisation appelée par le chargeur de module eXo.
 * Initialise Vue et i18n avant de monter le composant racine.
 */
export function init() {
  console.log('[DemandeConge] Initialisation du portlet (Standard eXo Pattern)...');
  
  // Vérification de la présence du framework eXo (Injecté globalement par GateIn)
  if (typeof exoi18n !== 'undefined' && typeof Vue !== 'undefined') {
    exoi18n.loadLanguageAsync(lang, url)
      .then((i18n) => {
        console.log('[DemandeConge] i18n chargé avec succès. Montage de l\'application.');
        
        // Création de l'instance Vue 3
        Vue.createApp({
          template: `<conge-main id="${appId}" />`,
          i18n, // Injection des traductions
        }, `#${appId}`, 'Demande Conge Application');
      })
      .catch((err) => {
        // Fallback : Montage sans i18n (Mode dégradé)
        console.warn('[DemandeConge] i18n non disponible. Montage dégradé.', err);
        Vue.createApp({
          template: `<conge-main id="${appId}" />`,
        }, `#${appId}`, 'Demande Conge Application');
      });
  } else {
    console.error('[DemandeConge] Dépendances eXo (Vue ou exoi18n) absentes du scope global.');
  }
}

/**
 * Auto-initialisation pour environnement hors-portail (Localhost / Tests).
 * Si 'define' n'est pas présent, on lance l'init immédiatement.
 */
if (typeof define !== 'function' || !define.amd) {
    init();
}
