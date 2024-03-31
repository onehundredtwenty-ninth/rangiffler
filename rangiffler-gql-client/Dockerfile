FROM node:19.4.0-alpine AS build
ARG NPM_COMMAND
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . ./
RUN npm run ${NPM_COMMAND}
RUN ls

# release step
FROM nginx:1.23.3-alpine AS release
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html/
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
