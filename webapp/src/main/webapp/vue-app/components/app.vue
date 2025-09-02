<template>
  <div id="vue_webpack_demande_conge">
    <header>
      <h1>Bienvenue sur votre Espace Congés</h1>
      <p>Gérez facilement vos demandes de congés en quelques clics</p>

      <button @click="awesome = !awesome" class="btn">Faire une demande</button>
    </header>

    <demandeCongeForm />
  </div>
</template>

<script setup>
import demandeCongeForm from './demandeCongeForm.vue';
</script>

<style scoped>
header {
  height: auto;
  background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
    url("https://images.unsplash.com/photo-1521737604893-d14cc237f11d")
      no-repeat center center/cover;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
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
</style>
