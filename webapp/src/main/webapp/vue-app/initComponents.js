import demandeCongeForm from './components/demandeCongeForm.vue';
import demandeCongeList from './components/demandeCongeList.vue';
import demandeCongeListAll from './components/demandeCongeListAll.vue';

const components = {
  'demandeCongeApp-form': demandeCongeForm,
  'demandeCongeApp-list': demandeCongeList,
  'demandeCongeApp-list-all': demandeCongeListAll,
};

for (const key in components) {
  Vue.component(key, components[key]);
}