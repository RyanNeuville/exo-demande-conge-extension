import Vue from 'vue';
import "./initComponents.js";
import app from "./components/congeAppMain.vue";
import router from "./router.js";
import "./../css/demandeCongeApp.css";

/* Configuration i18n eXo */
const lang = (eXo && eXo.env && eXo.env.portal && eXo.env.portal.language) || "fr";
const resourceBundleName = "locale.portlet.demandeCongeApp";
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;

/* Initialisation de l'application */
exoi18n.loadLanguageAsync(lang, url).then((i18n) => {
  window.vm = new Vue({
    router,
    render: (h) => h(app),
    i18n,
  }).$mount("#demandeCongeApp");
});
