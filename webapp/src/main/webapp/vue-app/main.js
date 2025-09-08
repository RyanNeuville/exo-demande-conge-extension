/* importer le fichier d'initialisation des composants */
import './initComponents.js';

/* importer l'application VueJS principale */
import app from './components/congeAppMain.vue';


/* importer le fichier CSS pour l'application */
import './../css/demandeCongeApp.css';

/* Obtenir la langue de l'utilisateur */
const lang = eXo && eXo.env && eXo.env.portal && eXo.env.portal.language || 'en';

/* Obtenir le nom du groupe de ressources */
const resourceBundleName = 'locale.portlet.demandeCongeApp';

/* Obtenir l'URL pour charger les bundles de ressources */
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;

/* obtenir les ensembles de ressources */
exoi18n.loadLanguageAsync(lang, url)
    .then(i18n => {
    /* initialiser l'application Vue lorsque les ressources locales sont prêtes */
        new Vue({
            render: h => h(app),
            i18n
        }).$mount('#demandeCongeApp'); /* monter l'application sur l'élément HTML avec id = 'vue_webpack_demande_conge' */
    });

setInterval(() => {
  vm.$children[0].$children.forEach(child => {
    if (child.fetchMyDemandes) child.fetchMyDemandes();
    if (child.fetchRelationsDemandes) child.fetchRelationsDemandes();
    if (child.fetchAllDemandes) child.fetchAllDemandes();
  });
}, 3000);