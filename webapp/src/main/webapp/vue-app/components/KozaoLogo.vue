<template>
  <svg :width="width" :height="height" viewBox="0 0 120 40" xmlns="http://www.w3.org/2000/svg">
    <!-- Orange 'k' -->
    <path d="M10 5V35" stroke="#EA580C" stroke-width="6" stroke-linecap="round" />
    <path d="M10 20L25 5M10 20L25 35" stroke="#EA580C" stroke-width="6" stroke-linecap="round" />
    
    <!-- Navy 'ozao' -->
    <text x="32" y="28" fill="#1E293B" style="font: bold 26px sans-serif; letter-spacing: -1px;">ozao</text>
  </svg>
</template>

<script>
export default {
  props: {
    width: { type: [String, Number], default: 120 },
    height: { type: [String, Number], default: 40 }
  }
};
</script>
