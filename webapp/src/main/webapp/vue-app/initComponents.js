import congeAppMain from './components/congeAppMain.vue';
import congeForm from './components/congeForm.vue';
import congeFeatures from './components/congeFeatures.vue';
import congeList from './components/congeList.vue';
import congeAdminList from './components/congeAdminList.vue';

const components = {
    'conge-main': congeAppMain,
    'conge-form': congeForm,
    'conge-features': congeFeatures,
    'conge-list': congeList,
    'conge-admin-list': congeAdminList
};

for (const key in components) {
    Vue.component(key, components[key]);
}