<template>
<ds-create-channel-modal />
<ds-create-feed-modal />
<ds-edit-profile-modal />

<v-container
    class="profile-page"
    style="max-width: 1000px;"
>
    <!-- Верхняя часть профиля -->
    <v-row>
        <v-col
            cols="12"   
            class="d-flex align-center"
        >
            <v-avatar
                size="100"
                icon="$vuetify"
            >
                <!-- <img
                    :src="userStore.self?.avatar"
                    alt="User avatar"
                > -->
            </v-avatar>
            <div class="ml-4">
                <h3>{{ userStore.self?.nickname }}</h3>
                <span style="color: var(--color-fontSecondary)">{{ userStore.self?.email }}</span>
                <p
                    style="overflow: hidden;
                            display: -webkit-box;
                            -webkit-line-clamp: 5; /* number of lines to show */
                                    line-clamp: 5; 
                            -webkit-box-orient: vertical;"
                >
                    {{ userStore.self?.about }}
                </p>
            </div>
            <v-spacer />
            <v-btn
                color="orange"
                @click="editProfileDialogStore.openDialog()"
            >
                Редактировать
            </v-btn>
        </v-col>
    </v-row>
  
    <!-- Разделитель -->
    <v-divider
        class="mt-10 mb-10"
        style="border: 1px solid black"
    />
  
    <!-- Переключатель "Каналы" и "Ленты" -->
    <v-row class="mt-4">
        <v-col cols="12">
            <v-btn-toggle
                v-model="profileStore.selectedTab"
                mandatory
            >
                <v-btn value="channels">
                    Каналы
                </v-btn>
                <v-btn value="feeds">
                    Ленты
                </v-btn>
            </v-btn-toggle>
        </v-col>
    </v-row>

    <v-spacer class="mb-10" />
  
    <!-- Сетка с карточками -->
    <v-row>
        <v-col
            cols="12"
            md="4"
        >
            <v-card
                class="pa-4 d-flex align-center justify-center cursor-pointer"
                color="#ffe4e1"
                height="200"
                variant="flat"
                @click="createItem"
            >
                <div>
                    <v-icon>mdi-plus</v-icon>
                    <span class="ml-2">Создать {{ profileStore.selectedTab === 'channels' ? 'канал' : 'ленту' }}</span>
                </div>
            </v-card>
        </v-col>
        <v-col
            v-for="(item, index) in getItems()"
            :key="index"
            cols="12"
            md="4"
        >
            <ds-channel-preview 
                v-if="profileStore.selectedTab === 'channels'" 
                :name="item.name"
                @open="openItem(index)"
            ></ds-channel-preview>

            <ds-feed-preview 
                v-else 
                :name="item.name"
                @open="openItem(index)"
            ></ds-feed-preview>
        </v-col>
    </v-row>
</v-container>
</template>
  
<script lang=ts setup>
import DsCreateChannelModal from '@/components/DsCreateChannelModal.vue';
import { useChannelStore } from '@/stores/channel-store';
import { useCreateChannelDialogStore } from '@/stores/create-channel-dialog-store';
import { useCreateFeedDialogStore } from '@/stores/create-feed-dialog-store';
import { useEditProfileDialogStore } from '@/stores/edit-profile-dialog-store';
import { useFeedStore } from '@/stores/feed-store';
import { useProfileStore } from '@/stores/profile-store';
import { useUserStore } from '@/stores/user-store';
import { ref } from 'vue';

const userStore = useUserStore();
const channelStore = useChannelStore();
const feedStore = useFeedStore();
const profileStore = useProfileStore();
const router = useRouter();

const channelDialogStore = useCreateChannelDialogStore();
const feedDialogStore = useCreateFeedDialogStore();
const editProfileDialogStore = useEditProfileDialogStore();

userStore.fetchSelf();
watch(() => userStore.self, () => {
    channelStore.fetchSelfChannels();
    feedStore.fetchSelfFeeds();
});

function getItems() {
    return profileStore.selectedTab === 'channels' ? channelStore.selfChannels : feedStore.selfFeeds;
}

function createItem() {
    if (profileStore.selectedTab == 'channels') {
        channelDialogStore.shown = true;
    } else {
        feedDialogStore.shown = true;
    }
}

function openItem(idx: number) {
    const id = getItems()?.[idx]?.id;
    if (!id) {
        console.log('no id');
        return;
    }
    if (profileStore.selectedTab === 'channels') {
        router.push({ name: 'channels/:id', params: { id } });
    } else {
        router.push({ name: 'feeds/:id', params: { id } });
    }
}
</script>

<style scoped>
.profile-page h2 {
    margin: 0;
}
</style>