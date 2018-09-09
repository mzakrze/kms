all:
	build & start

build-frontend:
	docker container rm fe
	docker container run --name fe -it -p 3000:3000 \
		-v $(shell pwd)/../VERSION:/usr/frontend_app/VERSION \
		-v $(shell pwd)/package.json:/usr/frontend_app/package.json \
		-v $(shell pwd)/webpack.config.js:/usr/frontend_app/webpack.config.js \
		-v $(shell pwd)/webpack.prod.js:/usr/frontend_app/webpack.prod.js \
		-v $(shell pwd)/.babelrc:/usr/frontend_app/.babelrc \
		-v $(shell pwd)/.flowconfig:/usr/frontend_app/.flowconfig \
		-v $(shell pwd)/src:/usr/frontend_app/src \
		-v $(shell pwd)/vendor:/usr/frontend_app/vendor \
		-v $(shell pwd)/dist:/usr/frontend_app/dist \
	 	fe /bin/bash -c "npm run production-build; cp -R /dist ." 

start:
	docker container run --name fe -it -p 3000:3000 \
		-v $(shell pwd)/../VERSION:/usr/frontend_app/VERSION \
		-v $(shell pwd)/package.json:/usr/frontend_app/package.json \
		-v $(shell pwd)/webpack.config.js:/usr/frontend_app/webpack.config.js \
		-v $(shell pwd)/.babelrc:/usr/frontend_app/.babelrc \
		-v $(shell pwd)/.flowconfig:/usr/frontend_app/.flowconfig \
		-v $(shell pwd)/src:/usr/frontend_app/src \
		-v $(shell pwd)/vendor:/usr/frontend_app/vendor \
	 	fe npm run start
	 

stop: # TODO - make it gracefull
	docker container kill fe
	docker container rm fe

build:
	docker image build --tag fe .

sh:
	docker container exec -it fe bash