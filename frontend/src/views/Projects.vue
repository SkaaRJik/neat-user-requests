<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div>
        <!-- Full screen template       -->
        <div class="d-none d-lg-block">
            <projects-view-full-screen/>
        </div>
        <!-- Mobile version  -->
        <div class="d-md-none">
            <projects-view-mobile/>

        </div>
    </div>
</template>

<script>
    import ProjectsViewFullScreen from "../components/projects/projects-views/ProjectsViewFullScreen";
    import ProjectsViewMobile from "../components/projects/projects-views/ProjectsViewMobile";
    export default {
        name: "Projects",
        components: {ProjectsViewMobile, ProjectsViewFullScreen},
        props:{
            shared: Boolean
        },
        methods: {

        },
        data () {
            return {

            }
        },
    }
</script>

<style scoped>

</style>
