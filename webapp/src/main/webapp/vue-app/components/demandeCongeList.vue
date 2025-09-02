<template>
  <!-- Liste des demandes de congés -->
  <section class="conges-section">
    <h2>Mes Demandes de Congés</h2>

    <table class="conges-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Date Début</th>
          <th>Date Fin</th>
          <th>Type</th>
          <th>Statut</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>1</td>
          <td>2025-09-05</td>
          <td>2025-09-10</td>
          <td>Annuel</td>
          <td><span class="statut en-attente">En attente</span></td>
          <td><button class="btn-annuler">Annuler</button></td>
        </tr>
        <tr>
          <td>2</td>
          <td>2025-08-20</td>
          <td>2025-08-25</td>
          <td>Maladie</td>
          <td><span class="statut approuve">Approuvé</span></td>
          <td><button class="btn-annuler" disabled>Annuler</button></td>
        </tr>
        <tr>
          <td>3</td>
          <td>2025-07-15</td>
          <td>2025-07-20</td>
          <td>Annuel</td>
          <td><span class="statut refuse">Refusé</span></td>
          <td><button class="btn-annuler" disabled>Annuler</button></td>
        </tr>
      </tbody>
    </table>
  </section>
</template>

<script>
export default {
  
};
</script>

<style>
.conges-section {
  margin: 20px;
  font-family: Arial, sans-serif;
}

.conges-section h2 {
  margin-bottom: 15px;
  color: #333;
}

.conges-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.conges-table th,
.conges-table td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.conges-table th {
  background: #f5f5f5;
  font-weight: bold;
}

.statut {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 0.9em;
  color: #fff;
}

.en-attente {
  background: orange;
}

.approuve {
  background: green;
}

.refuse {
  background: red;
}

.btn-annuler {
  background: #e74c3c;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.3s;
}

.btn-annuler:hover:not([disabled]) {
  background: #c0392b;
}

.btn-annuler:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
