all:
	build & start

start:
	docker container run --name fe -it -p 3000:3000  \
		-v $(shell pwd)/package.json:/usr/frontend_app/package.json \
		-v $(shell pwd)/webpack.config.js:/usr/frontend_app/webpack.config.js \
		-v $(shell pwd)/webpack.prod.js:/usr/frontend_app/webpack.prod.js \
		-v $(shell pwd)/.babelrc:/usr/frontend_app/.babelrc \
		-v $(shell pwd)/.flowconfig:/usr/frontend_app/.flowconfig \
		-v $(shell pwd)/src:/usr/frontend_app/src \
	 	fe npm run start

stop: # TODO - make it gracefull
	docker container kill fe
	docker container rm fe

build:
	docker image build --tag fe .

sh:
	docker container exec -it fe bash