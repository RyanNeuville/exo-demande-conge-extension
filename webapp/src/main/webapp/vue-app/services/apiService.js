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
      const errorData = await response.json().catch(() => null);
      throw { response: { data: errorData, status: response.status } };
    }
    if (response.status === 204) {
      return { data: null };
    }
    const data = await response.json().catch(() => null);
    return { data, status: response.status };
  } catch (error) {
    console.error(`[API Error]`, error);
    throw error;
  }
};

export default {
  /** --- Demandes --- **/
  getMesDemandes: () => fetchApi('/demandes/me', { method: 'GET' }),
  getDemandesATraiter: () => fetchApi('/demandes/a-traiter', { method: 'GET' }),
  getToutesLesDemandes: () => fetchApi('/demandes/toutes', { method: 'GET' }),
  getDemande: (id) => fetchApi(`/demandes/${id}`, { method: 'GET' }),
  soumettreDemande: (data) => fetchApi('/demandes', { method: 'POST', body: data }),
  validerDemande: (id, commentaire) => fetchApi(`/demandes/${id}/valider`, { method: 'POST', body: { commentaire } }),
  refuserDemande: (id, commentaire) => fetchApi(`/demandes/${id}/refuser`, { method: 'POST', body: { commentaire } }),
  annulerDemande: (id) => fetchApi(`/demandes/${id}/annuler`, { method: 'POST' }),

  /** --- Utilisateurs & Solde --- **/
  getMonSolde: () => fetchApi('/utilisateurs/me/solde', { method: 'GET' }),
  getUtilisateurs: () => fetchApi('/utilisateurs/me', { method: 'GET' }),

  /** --- Types de Congés --- **/
  getTypesConges: () => fetchApi('/types-conges', { method: 'GET' })
};
