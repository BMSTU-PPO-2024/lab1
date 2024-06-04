<template>
    <div class="profile">
        <div class="profile_header">
            <div class="profile_avatar">
                <img
                    :src="usersStore.currentUser.avatarUrl"
                    alt="Your profile image"
                    class="profile_avatar-img"
                >
            </div>
            <div class="profile_self-info">
                <div class="profile_nickname">
                    {{ usersStore.currentUser.nickname }}
                </div>
                <div class="profile_about">
                    {{ usersStore.currentUser.about }}
                </div>
            </div>
            <div class="profile_header-buttons">
                <sk-button
                    v-if="isSelf"
                    class="profile_change-data-button"
                >
                    Изменить данные
                </sk-button>
            </div>
        </div>
        <div class="profile_divider-line">

        </div>
        <div class="profile_collections-select">
            <sk-radio-group
                :variants="collectionTypes"
                :selected="selectedType"
                @update:selected="collectionSelected"
            >

            </sk-radio-group>
        </div>
            <div class="profile_self-collections">
            <sk-collection-card
                v-if="isSelf"
                :has-burger="false"
                :name="'Добавить'"
                @click="addNewCollection"
            ></sk-collection-card>
            <sk-collection-card
                v-for="collection in collections"
                :name="collection.name"
                :has-burger="true"
                @click.self="openCollection(collection.id)"
            >
                <div class="profile_collection-popup">
                    <sk-button
                        @click="deleteCollection(collection.id)"
                    >
                        Удалить
                    </sk-button>
                </div>
            </sk-collection-card>
        </div>

        <sk-modal
            v-model:show="showCreateCollectionModal"
        >
            <div class="profile_create-collection">
                <sk-input
                    :placeholder="'Введите название'"
                    v-model="currentName"
                />
                <sk-button
                    @click="createCollection"
                >
                    Создать
                </sk-button>
            </div>
        </sk-modal>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useUserStore} from "@/stores/userStore";
import SkCollectionCard from "@/components/CollectionCard.vue";
import {channelApi} from "@/api/channel"
import {usePostStore} from "@/stores/postStore";
import {useFeedStore} from "@/stores/feedStore";
import {useChannelStore} from "@/stores/channelStore";
import {useUsersStore} from "@/stores/usersStore";
import collectionCard from "@/components/CollectionCard.vue";


export default defineComponent({
    name: "Profile",
    components: {
        SkCollectionCard
    },


    props: {
        userId: {
            type: String,
            required: false,
        }
    },

    setup() {
        const userStore = useUserStore();
        const usersStore = useUsersStore();
        // const postStore = usePostStore();
        const channelStore = useChannelStore();
        const feedStore = useFeedStore();

        return {userStore, usersStore, channelStore, feedStore};
    },

    data: () => ({
        typesMap: {
            "Каналы": "channel",
            "Ленты": "feed"
        },
        selectedType: 'Каналы',
        showCreateCollectionModal: false,
        currentCollectionId: "",
        currentName: "",
    }),

    computed: {
        collectionTypes(): string[] {
            return Object.keys(this.typesMap);
        },
        collections() {
            // console.log("this.selectedType:", this.selectedType);
            if (this.selectedType == "Каналы") {
                // console.log("return:", this.channelStore.currentUserChannels);
                return this.channelStore.currentUserChannels;
            }
            // console.log("return:", this.feedStore.currentUserFeeds);
            return this.feedStore.currentUserFeeds;
        },
        isSelf() {
            return !this.userId || this.userId == this.userStore.id;
        }
    },
    methods: {
        collectionSelected(type: string) {
            this.selectedType = type;
            // this.selectedCollectionType = (this.typesMap as any)[type] as string;
        },
        addNewCollection() {
            this.showCreateCollectionModal = true;
        },
        createCollection () {
            if (this.selectedType == "Каналы") {
                this.channelStore.currentChannelName = this.currentName;
                this.channelStore.create();
            } else {
                this.feedStore.create(this.currentName);
            }
        },
        deleteCollection(id: string) {
            // console.log("delete collection ", id);
            if (this.selectedType == "Каналы") {
                this.channelStore.remove(id);
            } else {
                this.feedStore.remove(id);
            }
        },
        openCollection (id: string) {
            if (this.selectedType == "Каналы") {
                this.$router.push(`/channel/${id}`)
            } else {
                this.$router.push(`/feed/${id}`)
            }
        }
    },
    created() {
        // console.log("profile created");
        // channelApi.listChannels({ id: this.userStore.id, page: 20 })
        //     .then((res) => {
        //         // console.log(res);
        //     });
        let userId = this.userId ? this.userId : this.userStore.id;
        this.usersStore.setCurrentUser(userId);
        this.channelStore.setCurrentUser(userId);
        this.feedStore.setCurrentUser(userId);
    }
});
</script>

<style scoped>
.profile {
    width: 100%;
    min-height: 80vh;
}

.profile_avatar {
    display: inline-block;
    /*border: 2px solid red;*/
    margin-left: 10%;
}

.profile_avatar-img {
    width: 10em;
    height: 10em;
    object-fit: cover;
    border-radius: 50%;
}

.profile_self-info {
    display: inline-block;
    max-width: 100%;
    word-wrap: normal;
    /*border: 2px solid blue;*/
}

.profile_about {
    max-height: 10em;
    overflow-y: auto;
    overflow-x: hidden;
    /*text-overflow: ellipsis;*/
}

.profile_header {
    /*border: 2px solid green;*/
    display: flex;
    /*justify-content: center;*/
    gap: 4em;
}

.profile_nickname {
    font-weight: bold;
    font-size: 3em;
}

.profile_change-data-button {
    min-width: fit-content;
    text-wrap: nowrap;
}

.profile_header-buttons {
    margin-top: 1em
}

.profile_divider-line {
    min-width: 100%;
    border: 1px solid black;
    margin: 1em 0;
}
.profile_collections-select {
    margin-bottom: 1em;
}
.profile_self-collections {
    display: flex;
    gap: 2em;
    flex-wrap: wrap;
    /*margin: 0 auto;*/
    /*justify-content: center;*/
    /*align-content: center;*/
}
.profile_create-collection {
    padding: 1em;
}
</style>