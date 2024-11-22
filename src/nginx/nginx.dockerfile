FROM nginx
RUN sed -i 's/Server: nginx/Server: dsprk/' `which nginx`
RUN mkdir -p -m a=rwx /data/nginx/
