import demandeCongeAppMain from './components/demandeCongeAppMain.vue';
import demandeCongeAppFilesLists from './components/demandeCongeAppFilesLists.vue';

const components = {
  'demandeCongeApp-main': demandeCongeAppMain,
  'demandeCongeApp-files-lists': demandeCongeAppFilesLists,
};

for (const key in components) {
  Vue.component(key, components[key]);
}
