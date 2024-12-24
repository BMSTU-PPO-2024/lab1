<template>
<v-container style="max-width: 1200px;">
    <v-row class="w-100 justify-center pt-8 pb-8">
        <h3>Найти в DevSpark</h3>
    </v-row>
    <v-row class="d-flex justify-center">
        <v-col
            sm="8"
            lg="6"
        >
            <v-btn-toggle
                v-model="mainStore.selectedTab"
                mandatory
                class="pb-2"
            >
                <v-btn value="channels">
                    Каналы
                </v-btn>
                <v-btn value="feeds">
                    Ленты
                </v-btn>
            </v-btn-toggle>
            <v-text-field 
                v-model="mainStore.searchQuery"
                label="Поиск"
            />
        </v-col>
    </v-row>

    <v-row>
        <v-col
            v-for="(item, index) in items"
            :key="item.id"
            cols="12"
            sm="6"
            md="4"
        >
            <ds-channel-preview 
                v-if="mainStore.selectedTab === 'channels'" 
                :name="item.name"
                @open="openItem(index)"
            />

            <ds-feed-preview 
                v-else 
                :name="item.name"
                @open="openItem(index)"
            />
        </v-col>
    </v-row>
</v-container>
</template>

<script setup lang="ts">
import type { Channel, Feed } from '@/api/types';
import router from '@/router';
import { useChannelStore } from '@/stores/channel-store';
import { useFeedStore } from '@/stores/feed-store';
import { useMainStore } from '@/stores/main-store';


const channelStore = useChannelStore();
const feedStore = useFeedStore();
const mainStore = useMainStore();

watch(() => [mainStore.searchQuery, mainStore.selectedTab], () => {
    if (mainStore.selectedTab === 'channels') {
        channelStore.getChannels({
            batch: 100,
            pattern: mainStore.searchQuery,
        })
    } else {
        feedStore.getFeeds({
            batch: 100,
            pattern: mainStore.searchQuery,
        })
    }
}, { immediate: true });


const items = computed<Channel[] | Feed[] | undefined>(() => mainStore.selectedTab == 'channels' 
                                ? channelStore.currentChannels
                                : feedStore.currentFeeds);

const openItem = (idx: number) => {
    console.log('open item', mainStore.selectedTab, feedStore.currentFeeds?.[idx], channelStore.currentChannels?.[idx])
    if (mainStore.selectedTab === 'channels') {
        router.push({
            name: 'channels/:id',
            params: {
                id: channelStore.currentChannels?.[idx].id ?? '' 
            }
        })
    } else {
        router.push({
            name: 'feeds/:id',
            params: {
                id: feedStore.currentFeeds?.[idx].id ?? '' 
            }
        })
    }
}

</script>