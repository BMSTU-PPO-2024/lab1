const CACHE_NAME = "my-pwa-cache-v1";

self.addEventListener("install", event => {
    console.log("Service Worker installed");
});

self.addEventListener("activate", event => {
    console.log("Service Worker activated");
    event.waitUntil(
        caches.keys().then(cacheNames =>
            Promise.all(
                cacheNames.map(cacheName => {
                    if (cacheName !== CACHE_NAME) {
                        return caches.delete(cacheName);
                    }
                })
            )
        )
    );
});

self.addEventListener("fetch", event => {
    if (!event.request.url.startsWith('http')) {
        return;
    }
    event.respondWith(
        fetch(event.request)
            .then(networkResponse => {
                return caches.open(CACHE_NAME).then(cache => {
                    cache.put(event.request, networkResponse.clone());
                    return networkResponse;
                });
            })
            .catch(() => {
                return caches.match(event.request).then(cacheResponse => {
                    if (cacheResponse) {
                        return cacheResponse;
                    }
                    return caches.match('/offline.html');
                });
            })
    );
});
