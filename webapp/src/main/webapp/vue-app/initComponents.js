import demandeCongeAppMain from './components/demandeCongeAppMain.vue';
import demandeCongeAppFilesLists from './components/demandeCongeAppFilesLists.vue';
import demandeCongeForm from './components/demandeCongeForm.vue';
import demandeCongeList from './components/demandeCongeList.vue';
import demandeCongeListAll from './components/demandeCongeListAll.vue';

const components = {
  'demandeCongeApp-form': demandeCongeForm,
  'demandeCongeApp-list': demandeCongeList,
  'demandeCongeApp-list-all': demandeCongeListAll,
  'demandeCongeApp-main': demandeCongeAppMain,
  'demandeCongeApp-files-lists': demandeCongeAppFilesLists,
};

for (const key in components) {
  Vue.component(key, components[key]);
}
