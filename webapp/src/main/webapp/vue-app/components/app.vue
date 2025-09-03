<template>
  <div id="vue_webpack_demande_conge">
    <header>
      <h1>Bienvenue sur votre Espace Congés</h1>
      <p>Gérez facilement vos demandes de congés en quelques clics</p>

      <button @click="awesome = !awesome" class="btn">Faire une demande</button>
    </header>

    <!-- Composant DemandeCongeForm -->
    <div class="conge-form-container">
      <h2>Demande de Congé</h2>
      <form class="conge-form">
        <!-- Date de début -->
        <div class="form-group">
          <label for="date-debut">Date de début :</label>
          <input type="date" id="date-debut" name="date-debut" required />
        </div>

        <!-- Date de fin -->
        <div class="form-group">
          <label for="date-fin">Date de fin :</label>
          <input type="date" id="date-fin" name="date-fin" required />
        </div>

        <!-- Type de congé -->
        <div class="form-group">
          <label for="type-conge">Type de congé :</label>
          <select id="type-conge" name="type-conge" required>
            <option value="">-- Sélectionner --</option>
            <option value="CONGE_ANNUEL">Congé Annuel</option>
            <option value="CONGE_MALADIE">Congé Maladie</option>
            <option value="CONGE_MATERNITE">Congé Maternité</option>
            <option value="CONGE_PATERNITE">Congé Paternité</option>
            <option value="CONGE_REPOS">Congé Repos</option>
            <option value="CONGE_VACANCES">Congé Vacances</option>
            <option value="CONGE_FORMATION">Congé Formations</option>
            <option value="AUTRE_CONGE">Autre</option>
          </select>
        </div>

        <!-- Motif -->
        <div class="form-group">
          <label for="motif">Motif :</label>
          <textarea
            id="motif"
            name="motif"
            rows="4"
            placeholder="Expliquez brièvement la raison..."
            required
          ></textarea>
        </div>

        <!-- Boutons -->
        <div class="form-actions">
          <button type="reset" class="btn cancel">Annuler</button>
          <button type="submit" class="btn submit">Envoyer</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import demandeCongeForm from './demandeCongeForm.vue';
import {ref} from 'vue';

const awesome = ref(false)
</script>

<style scoped>
header {
  height: 20vh;
  background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
    url("https://images.unsplash.com/photo-1521737604893-d14cc237f11d")
      no-repeat center center/cover;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 0 20px;
}

header h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  animation: fadeInDown 1s ease-in-out;
}

header p {
  font-size: 1.3rem;
  margin-bottom: 30px;
  animation: fadeInUp 1.5s ease-in-out;
}

.btn {
  padding: 12px 30px;
  background-color: #ff9800;
  color: #fff;
  text-decoration: none;
  border-radius: 30px;
  font-weight: bold;
  transition: 0.3s;
  animation: fadeInUp 2s ease-in-out;
}

.btn:hover {
  background-color: #e68900;
  transform: scale(1.05);
}

/* Animations */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Style css pour le formulaire */

.conge-form-container {
  max-width: 500px;
  margin: 40px auto;
  padding: 25px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;

  h2 {
    text-align: center;
    margin-bottom: 20px;
    color: #333;
  }

  .conge-form .form-group {
    margin-bottom: 15px;
  }

  .conge-form label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    color: #444;
  }

  .conge-form input,
  .conge-form select,
  .conge-form textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-size: 14px;
  }

  .conge-form input:focus,
  .conge-form select:focus,
  .conge-form textarea:focus {
    border-color: #007bff;
    outline: none;
  }

  .form-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
  }

  .btn {
    padding: 10px 18px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-weight: bold;
  }

  .btn.cancel {
    background: #dc3545;
    color: #fff;
  }

  .btn.submit {
    background: #007bff;
    color: #fff;
  }

  .btn:hover {
    opacity: 0.9;
  }
}
</style>
