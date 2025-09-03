<template>
  <div id="demandeCongeListAll">
    <h2>Liste des demandes de congés</h2>
    <table>
      <thead>
        <tr>
          <th>Employé</th>
          <th>Date Début</th>
          <th>Date Fin</th>
          <th>Type de Congé</th>
          <th>Statut</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>Jean Dupont</td>
          <td>2025-09-05</td>
          <td>2025-09-10</td>
          <td>Congé annuel</td>
          <td><span style="color: orange">En attente</span></td>
          <td>
            <button class="btn btn-approve">Approuver</button>
            <button class="btn btn-reject">Refuser</button>
            <button class="btn btn-details">Détails</button>
          </td>
        </tr>
        <tr>
          <td>Marie Claire</td>
          <td>2025-09-15</td>
          <td>2025-09-18</td>
          <td>Congé maladie</td>
          <td><span style="color: red">Refusé</span></td>
          <td>
            <button class="btn btn-approve">Approuver</button>
            <button class="btn btn-reject">Refuser</button>
            <button class="btn btn-details">Détails</button>
          </td>
        </tr>
        <tr>
          <td>Ali Koné</td>
          <td>2025-09-20</td>
          <td>2025-09-25</td>
          <td>Congé maternité</td>
          <td><span style="color: green">Approuvé</span></td>
          <td>
            <button class="btn btn-approve">Approuver</button>
            <button class="btn btn-reject">Refuser</button>
            <button class="btn btn-details">Détails</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  
};
</script>

<style scoped>
h2 {
  text-align: center;
  color: #333;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

thead {
  background: #2c3e50;
  color: white;
}

th,
td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: center;
}

tr:nth-child(even) {
  background: #f9f9f9;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.btn-approve {
  background: #27ae60;
  color: white;
}

.btn-reject {
  background: #c0392b;
  color: white;
}

.btn-details {
  background: #2980b9;
  color: white;
}

.btn:hover {
  opacity: 0.8;
}
</style>
