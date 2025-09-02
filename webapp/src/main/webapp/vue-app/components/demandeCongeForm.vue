<template>
  <div id="demandeCongeForm">
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

<script>
export default {
 
};
</script>

<style>
.conge-form-container {
  max-width: 500px;
  margin: 40px auto;
  padding: 25px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

.conge-form-container h2 {
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
</style>
