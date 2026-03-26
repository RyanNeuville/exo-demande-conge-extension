import axios from 'axios';

/**
 * Service API centralisé pour la gestion des requêtes vers le backend.
 * Gère automatiquement le préfixe d'URL eXo Platform et les en-têtes.
 */

/* Obtenir l'URL de base pour l'API REST eXo */
/* Note: Utilisation du double préfixe /api/api comme requis par le déploiement actuel */
const BASE_URL = `${eXo.env.portal.context}/${eXo.env.portal.rest}/api/api`;

const api = axios.create({
  baseURL: BASE_URL,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

/**
 * Intercepteur pour la gestion globale des erreurs.
 */
api.interceptors.response.use(
  response => response,
  error => {
    const message = error.response?.data?.message || "Une erreur technique est survenue.";
    /* Possibilité de déclencher un toast global ici via un EventBus ou un Store */
    console.error(`[API Error] ${message}`, error);
    return Promise.reject(error);
  }
);

export default {
  /** --- Demandes --- **/
  getMesDemandes() {
    return api.get('/demandes/me');
  },
  getDemandesATraiter() {
    return api.get('/demandes/a-traiter');
  },
  getToutesLesDemandes() {
    return api.get('/demandes/toutes');
  },
  getDemande(id) {
    return api.get(`/demandes/${id}`);
  },
  soumettreDemande(data) {
    return api.post('/demandes', data);
  },
  validerDemande(id, commentaire) {
    return api.post(`/demandes/${id}/valider`, { commentaire });
  },
  refuserDemande(id, commentaire) {
    return api.post(`/demandes/${id}/refuser`, { commentaire });
  },
  annulerDemande(id) {
    return api.post(`/demandes/${id}/annuler`);
  },

  /** --- Utilisateurs & Solde --- **/
  getMonSolde() {
    return api.get('/utilisateurs/me/solde');
  },
  getUtilisateurs() {
    return api.get('/utilisateurs/me');
  },

  /** --- Types de Congés --- **/
  getTypesConges() {
    return api.get('/types-conges');
  }
};
