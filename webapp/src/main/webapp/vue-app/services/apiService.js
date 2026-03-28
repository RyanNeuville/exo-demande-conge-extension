/**
 * Service API centralisé pour la gestion des requêtes vers le backend.
 * Gère automatiquement le préfixe d'URL eXo Platform et les en-têtes.
 * Utilise l'API native fetch() (comme dans le projet Alfresco) pour garantir
 * la compatibilité des sessions avec le portail.
 */

/* Obtenir l'URL de base pour l'API REST eXo */
const getBaseUrl = () => {
  try {
    const context = eXo?.env?.portal?.context || '/portal';
    const rest = eXo?.env?.portal?.rest || 'rest';
    return `${context}/${rest}/api/api`;
  } catch (e) {
    console.warn("[DemandeConge] Impossible de lire l'environnement eXo, repli sur /portal/rest/api/api");
    return '/portal/rest/api/api';
  }
};

const BASE_URL = getBaseUrl();

/**
 * Wrapper interne pour fetch qui simule la réponse Axios { data: ... }
 * afin de ne pas casser les composants existants.
 */
const fetchApi = async (endpoint, options = {}) => {
  const url = `${BASE_URL}${endpoint}`;
  const defaultOptions = {
    credentials: 'include', // Indispensable pour garder la session eXo !
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
  };

  const finalOptions = {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers,
    },
  };

  if (finalOptions.body && typeof finalOptions.body === 'object' && !(finalOptions.body instanceof FormData)) {
    finalOptions.body = JSON.stringify(finalOptions.body);
  }

  try {
    const response = await fetch(url, finalOptions);
    if (!response.ok && response.status !== 404) {
      let errorData;
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        errorData = await response.json().catch(() => null);
      } else {
        const text = await response.text().catch(() => null);
        errorData = { message: text };
      }
      throw { response: { data: errorData, status: response.status } };
    }
    if (response.status === 204) {
      return { data: null };
    }
    const data = await response.json().catch(() => null);
    return { data, status: response.status };
  } catch (error) {
    if (error.response) console.error(`[API Error ${error.response.status}]`, error.response.data);
    else console.error(`[API Error]`, error);
    throw error;
  }
};

export default {
  /** 
   * Récupère la liste des demandes de congés de l'utilisateur actuellement connecté.
   * @returns {Promise<{data: Array}>} Liste des objets DemandeCongeResponseDTO.
   */
  getMesDemandes: () => fetchApi('/demandes/me', { method: 'GET' }),

  /** 
   * Récupère les demandes que le manager connecté doit valider ou refuser.
   * @returns {Promise<{data: Array}>} Liste des demandes en attente de traitement.
   */
  getDemandesATraiter: () => fetchApi('/demandes/a-traiter', { method: 'GET' }),

  /** 
   * [ADMIN] Récupère l'intégralité des demandes de l'entreprise pour le rapport global.
   * @returns {Promise<{data: Array}>} Historique complet pour la traçabilité.
   */
  getToutesLesDemandes: () => fetchApi('/demandes/toutes', { method: 'GET' }),

  /** 
   * Récupère les détails complets d'une demande spécifique par son ID.
   * @param {string} id - Identifiant unique de la demande.
   */
  getDemande: (id) => fetchApi(`/demandes/${id}`, { method: 'GET' }),

  /** 
   * Soumet une nouvelle demande de congé au serveur.
   * @param {Object} data - Objet DemandeCongeDTO contenant dates, type et motif.
   */
  soumettreDemande: (data) => fetchApi('/demandes', { method: 'POST', body: data }),

  /** 
   * Modifie une demande existante avant qu'elle ne soit traitée.
   * @param {string} id - ID de la demande.
   * @param {Object} data - Nouvelles données de la demande.
   */
  modifierDemande: (id, data) => fetchApi(`/demandes/${id}`, { method: 'PUT', body: data }),

  /** 
   * Approuve officiellement une demande de congé.
   * @param {string} id - ID de la demande.
   * @param {string} commentaire - Raison de l'approbation (optionnel).
   */
  validerDemande: (id, commentaire) => fetchApi(`/demandes/${id}/valider`, { method: 'POST', body: { commentaire } }),

  /** 
   * Rejette une demande de congé.
   * @param {string} id - ID de la demande.
   * @param {string} commentaire - Motif obligatoire du refus.
   */
  refuserDemande: (id, commentaire) => fetchApi(`/demandes/${id}/refuser`, { method: 'POST', body: { commentaire } }),

  /** 
   * Annule une demande (action effectuée par le demandeur lui-même).
   * @param {string} id - ID de la demande à annuler.
   */
  annulerDemande: (id) => fetchApi(`/demandes/${id}`, { method: 'DELETE' }),

  /** 
   * Récupère l'historique complet des changements d'état (workflow) pour une demande.
   * @param {string} id - ID de la demande.
   */
  getHistoriqueDemande: (id) => fetchApi(`/demandes/${id}/historique`, { method: 'GET' }),

  /** 
   * Récupère le solde de congés annuel restant pour l'utilisateur connecté.
   * @returns {Promise<{data: Object}>} Objet contenant le champ 'solde'.
   */
  getMonSolde: () => fetchApi('/utilisateurs/me/solde', { method: 'GET' }),

  /** 
   * Récupère les informations de profil de base de l'utilisateur connecté.
   */
  getUtilisateurs: () => fetchApi('/utilisateurs/me', { method: 'GET' }),

  /** 
   * Liste les types de congés disponibles (Formation, CP, Maladie, etc.).
   */
  getTypesConges: () => fetchApi('/types-conges', { method: 'GET' })
};
