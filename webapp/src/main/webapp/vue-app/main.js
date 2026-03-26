import './initComponents.js';

/* Configuration i18n eXo */
const lang = eXo?.env?.portal?.language || 'fr';
const resourceBundleName = 'locale.portlet.demandeCongeApp';
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;
const appId = 'demandeCongeApp';

/* L'application est lancée via une fonction exportée pour être compatible AMD/Portlet */
export function init() {
  console.log('[DemandeConge] Initialisation du portlet (Standard eXo Pattern)...');
  
  if (typeof exoi18n !== 'undefined' && typeof Vue !== 'undefined') {
    exoi18n.loadLanguageAsync(lang, url)
      .then((i18n) => {
        console.log('[DemandeConge] i18n chargé avec succès. Montage de l\'application.');
        Vue.createApp({
          template: `<conge-main id="${appId}" />`,
          i18n,
        }, `#${appId}`, 'Demande Conge Application');
      })
      .catch((err) => {
        console.warn('[DemandeConge] i18n non disponible. Montage dégradé.', err);
        Vue.createApp({
          template: `<conge-main id="${appId}" />`,
        }, `#${appId}`, 'Demande Conge Application');
      });
  } else {
    console.error('[DemandeConge] Dépendances eXo (Vue ou exoi18n) absentes du scope global.');
  }
}

/* Auto-initialisation pour environnement hors-portail ou tests */
if (typeof define !== 'function' || !define.amd) {
    init();
}
