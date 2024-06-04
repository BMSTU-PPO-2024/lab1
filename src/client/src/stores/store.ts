import {defineStore} from "pinia";

export const useMainStore = defineStore("main",{
    state: () => ({
        rating: 0,
    }),
    getters: {

    },
    actions: {
        increaseRating(by: number) {
            this.rating += by;
        }
    }
});


// import {createStore} from "vuex";
//
// export default createStore({
//     state: () => ({
//         rating: 0,
//     }),
//     mutations: {
//         incRating(state) {
//             state.rating++;
//         }
//     }
// });