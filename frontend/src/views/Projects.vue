<template>
    <div>
        <!-- Full screen template       -->
        <div class="d-none d-lg-block">
            <projects-view-full-screen />
        </div>
        <!-- Mobile version  -->
        <div class="d-md-none">
            <projects-view-mobile/>

        </div>
    </div>
</template>

<script>
    import ProjectsViewFullScreen from "../components/projects-views/ProjectsViewFullScreen";
    import ProjectsViewMobile from "../components/projects-views/ProjectsViewMobile";
    export default {
        name: "Projects",
        components: {ProjectsViewMobile, ProjectsViewFullScreen},
        methods: {

        },
        data () {
            return {

            }
        },
    }
</script>

<style scoped>
    .v-progress-linear__bar, .v-progress-linear__bar__determinate {
        transition: none;
    }
</style>
