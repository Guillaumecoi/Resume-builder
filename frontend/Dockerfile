# Use Node.js LTS
FROM node:20-alpine

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Expose port for dev server
EXPOSE 4200

# Start dev server with hot reload
CMD ["npm", "run", "start", "--", "--host", "0.0.0.0", "--poll=2000", "--hmr"]